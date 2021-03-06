
/*
	* Copyright (C) 2002-2017 Sebastiano Vigna
	*
	* Licensed under the Apache License, Version 2.0 (the "License");
	* you may not use this file except in compliance with the License.
	* You may obtain a copy of the License at
	*
	*     http://www.apache.org/licenses/LICENSE-2.0
	*
	* Unless required by applicable law or agreed to in writing, software
	* distributed under the License is distributed on an "AS IS" BASIS,
	* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	* See the License for the specific language governing permissions and
	* limitations under the License.
	*/


package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.BigArrays;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;



/** A type-specific big list based on a big array; provides some additional methods that use polymorphism to avoid (un)boxing.
	*
	* <p>This class implements a lightweight, fast, open, optimized,
	* reuse-oriented version of big-array-based big lists. Instances of this class
	* represent a big list with a big array that is enlarged as needed when new entries
	* are created (by doubling the current length), but is
	* <em>never</em> made smaller (even on a {@link #clear()}). A family of
	* {@linkplain #trim() trimming methods} lets you control the size of the
	* backing big array; this is particularly useful if you reuse instances of this class.
	* Range checks are equivalent to those of {@link java.util}'s classes, but
	* they are delayed as much as possible. The backing big array is exposed by the
	* {@link #elements()} method.
	*
	* <p>This class implements the bulk methods {@code removeElements()},
	* {@code addElements()} and {@code getElements()} using
	* high-performance system calls (e.g., {@link
	* System#arraycopy(Object,int,Object,int,int) System.arraycopy()} instead of
	* expensive loops.
	*
	* @see java.util.ArrayList
	*/

public class LongBigArrayBigList extends AbstractLongBigList implements RandomAccess, Cloneable, java.io.Serializable {
	private static final long serialVersionUID = -7046029254386353130L;
	/** The initial default capacity of a big-array big list. */
	public static final int DEFAULT_INITIAL_CAPACITY = 10;







	/** The backing big array. */
	protected transient long a[][];

	/** The current actual size of the big list (never greater than the backing-array length). */
	protected long size;

	/** Creates a new big-array big list using a given array.
	 *
	 * <p>This constructor is only meant to be used by the wrapping methods.
	 *
	 * @param a the big array that will be used to back this big-array big list.
	 */

	protected LongBigArrayBigList(final long a[][], @SuppressWarnings("unused") boolean dummy) {
	 this.a = a;



	}

	/** Creates a new big-array big list with given capacity.
	 *
	 * @param capacity the initial capacity of the array list (may be 0).
	 */


	public LongBigArrayBigList(final long capacity) {
	 if (capacity < 0) throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
	 if (capacity == 0) a = LongBigArrays.EMPTY_BIG_ARRAY;
	 else a = LongBigArrays.newBigArray(capacity);



	}

	/** Creates a new big-array big list with {@link #DEFAULT_INITIAL_CAPACITY} capacity. */


	public LongBigArrayBigList() {
	 a = LongBigArrays.DEFAULT_EMPTY_BIG_ARRAY; // We delay allocation



	}

	/** Creates a new big-array big list and fills it with a given type-specific collection.
	 *
	 * @param c a type-specific collection that will be used to fill the array list.
	 */

	public LongBigArrayBigList(final LongCollection c) {
	 this(c.size());
	 for(LongIterator i = c.iterator(); i.hasNext();) add(i.nextLong());
	}

	/** Creates a new big-array big list and fills it with a given type-specific list.
	 *
	 * @param l a type-specific list that will be used to fill the array list.
	 */

	public LongBigArrayBigList(final LongBigList l) {
	 this(l.size64());
	 l.getElements(0, a, 0, size = l.size64());
	}

	/** Creates a new big-array big list and fills it with the elements of a given big array.
	 *
	 * <p>Note that this constructor makes it easy to build big lists from literal arrays
	 * declared as <code><var>type</var>[][] {{ <var>init_values</var> }}</code>.
	 * The only constraint is that the number of initialisation values is
	 * below {@link it.unimi.dsi.fastutil.BigArrays#SEGMENT_SIZE}.
	 *
	 * @param a a big array whose elements will be used to fill the array list.
	 */

	public LongBigArrayBigList(final long a[][]) {
	 this(a, 0, LongBigArrays.length(a));
	}

	/** Creates a new big-array big list and fills it with the elements of a given big array.
	 *
	 * <p>Note that this constructor makes it easy to build big lists from literal arrays
	 * declared as <code><var>type</var>[][] {{ <var>init_values</var> }}</code>.
	 * The only constraint is that the number of initialisation values is
	 * below {@link it.unimi.dsi.fastutil.BigArrays#SEGMENT_SIZE}.
	 *
	 * @param a a big array whose elements will be used to fill the array list.
	 * @param offset the first element to use.
	 * @param length the number of elements to use.
	 */

	public LongBigArrayBigList(final long a[][], final long offset, final long length) {
	 this(length);
	 LongBigArrays.copy(a, offset, this.a, 0, length);
	 size = length;
	}

	/** Creates a new big-array big list and fills it with the elements returned by an iterator..
	 *
	 * @param i an iterator whose returned elements will fill the array list.
	 */

	public LongBigArrayBigList(final Iterator<? extends Long> i) {
	 this();
	 while(i.hasNext()) this.add((i.next()).longValue());
	}

	/** Creates a new big-array big list and fills it with the elements returned by a type-specific iterator..
	 *
	 * @param i a type-specific iterator whose returned elements will fill the array list.
	 */

	public LongBigArrayBigList(final LongIterator i) {
	 this();
	 while(i.hasNext()) this.add(i.nextLong());
	}


	/** Returns the backing big array of this big list.
	 *
	 * @return the backing big array.
	 */

	public long[][] elements() {
	 return a;
	}
	/** Wraps a given big array into a big-array list of given size.
	 *
	 * @param a a big array to wrap.
	 * @param length the length of the resulting big-array list.
	 * @return a new big-array list of the given size, wrapping the given big array.
	 */

	public static LongBigArrayBigList wrap(final long a[][], final long length) {
	 if (length > LongBigArrays.length(a)) throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + LongBigArrays.length(a) + ")");
	 final LongBigArrayBigList l = new LongBigArrayBigList (a, false);
	 l.size = length;
	 return l;
	}

	/** Wraps a given big array into a big-array big list.
	 *
	 * @param a a big array to wrap.
	 * @return a new big-array big list wrapping the given array.
	 */

	public static LongBigArrayBigList wrap(final long a[][]) {
	 return wrap(a, LongBigArrays.length(a));
	}


	/** Ensures that this big-array big list can contain the given number of entries without resizing.
	 *
	 * @param capacity the new minimum capacity for this big-array big list.
	 */

	public void ensureCapacity(final long capacity) {
	 if (capacity <= a.length || a == LongBigArrays.DEFAULT_EMPTY_BIG_ARRAY) return;

	 a = LongBigArrays.forceCapacity(a, capacity, size);
	 assert size <= LongBigArrays.length(a);
	}

	/** Grows this big-array big list, ensuring that it can contain the given number of entries without resizing,
	 * and in case increasing current capacity at least by a factor of 50%.
	 *
	 * @param capacity the new minimum capacity for this big-array big list.
	 */

	private void grow(long capacity) {
	 final long oldLength = LongBigArrays.length(a);
	 if (capacity <= oldLength) return;
	 if (a != LongBigArrays.DEFAULT_EMPTY_BIG_ARRAY) capacity = Math.max(oldLength + (oldLength >> 1), capacity);
	 else if (capacity < DEFAULT_INITIAL_CAPACITY) capacity = DEFAULT_INITIAL_CAPACITY;

	 a = LongBigArrays.forceCapacity(a, capacity, size);
	 assert size <= LongBigArrays.length(a);
	}

	@Override
	public void add(final long index, final long k) {
	 ensureIndex(index);
	 grow(size + 1);
	 if (index != size) LongBigArrays.copy(a, index, a, index + 1, size - index);
	 LongBigArrays.set(a, index, k);
	 size++;
	 assert size <= LongBigArrays.length(a);
	}

	@Override
	public boolean add(final long k) {
	 grow(size + 1);
	 LongBigArrays.set(a, size++, k);
	 assert size <= LongBigArrays.length(a);
	 return true;
	}

	@Override
	public long getLong(final long index) {
	 if (index >= size) throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + size + ")");
	 return LongBigArrays.get(a, index);
	}

	@Override
	public long indexOf(final long k) {
	 for(long i = 0; i < size; i++) if (( (k) == (LongBigArrays.get(a, i)) )) return i;
	 return -1;
	}

	@Override
	public long lastIndexOf(final long k) {
	 for(long i = size; i-- != 0;) if (( (k) == (LongBigArrays.get(a, i)) )) return i;
	 return -1;
	}

	@Override
	public long removeLong(final long index) {
	 if (index >= size) throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + size + ")");
	 final long old = LongBigArrays.get(a, index);
	 size--;
	 if (index != size) LongBigArrays.copy(a, index + 1, a, index, size - index);



	 assert size <= LongBigArrays.length(a);
	 return old;
	}

	@Override
	public boolean rem(final long k) {
	 final long index = indexOf(k);
	 if (index == -1) return false;
	 removeLong(index);
	 assert size <= LongBigArrays.length(a);
	 return true;
	}

	@Override
	public long set(final long index, final long k) {
	 if (index >= size) throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + size + ")");
	 long old = LongBigArrays.get(a, index);
	 LongBigArrays.set(a, index, k);
	 return old;
	}


	@Override
	public boolean removeAll(final LongCollection c) {
	 long[] s = null, d = null;
	 int ss = -1, sd = BigArrays.SEGMENT_SIZE, ds = -1, dd = BigArrays.SEGMENT_SIZE;
	 for (long i = 0; i < size; i++) {
	  if (sd == BigArrays.SEGMENT_SIZE) {
	   sd = 0;
	   s = a[++ss];
	  }
	  if (!c.contains(s[sd])) {
	   if (dd == BigArrays.SEGMENT_SIZE) {
	    d = a[++ds];
	    dd = 0;
	   }
	   d[dd++] = s[sd];
	  }
	  sd++;
	 }
	 final long j = BigArrays.index(ds, dd);



	 final boolean modified = size != j;
	 size = j;
	 return modified;
	}



	@Override
	public boolean removeAll(final Collection<?> c) {
	 long[] s = null, d = null;
	 int ss = -1, sd = BigArrays.SEGMENT_SIZE, ds = -1, dd = BigArrays.SEGMENT_SIZE;
	 for (long i = 0; i < size; i++) {
	  if (sd == BigArrays.SEGMENT_SIZE) {
	   sd = 0;
	   s = a[++ss];
	  }
	  if (!c.contains(Long.valueOf(s[sd]))) {
	   if (dd == BigArrays.SEGMENT_SIZE) {
	    d = a[++ds];
	    dd = 0;
	   }
	   d[dd++] = s[sd];
	  }
	  sd++;
	 }
	 final long j = BigArrays.index(ds, dd);



	 final boolean modified = size != j;
	 size = j;
	 return modified;
	}

	@Override
	public void clear() {



	 size = 0;
	 assert size <= LongBigArrays.length(a);
	}

	@Override
	public long size64() {
	 return size;
	}

	@Override
	public void size(final long size) {
	 if (size > LongBigArrays.length(a)) a = LongBigArrays.forceCapacity(a, size, this.size);
	 if (size > this.size) LongBigArrays.fill(a, this.size, size, (0));



	 this.size = size;
	}

	@Override
	public boolean isEmpty() {
	 return size == 0;
	}

	/** Trims this big-array big list so that the capacity is equal to the size.
	 *
	 * @see java.util.ArrayList#trimToSize()
	 */
	public void trim() {
	 trim(0);
	}

	/** Trims the backing big array if it is too large.
	 *
	 * If the current big array length is smaller than or equal to
	 * {@code n}, this method does nothing. Otherwise, it trims the
	 * big-array length to the maximum between {@code n} and {@link #size64()}.
	 *
	 * <p>This method is useful when reusing big lists.  {@linkplain #clear() Clearing a
	 * big list} leaves the big-array length untouched. If you are reusing a big list
	 * many times, you can call this method with a typical
	 * size to avoid keeping around a very large big array just
	 * because of a few large transient big lists.
	 *
	 * @param n the threshold for the trimming.
	 */

	public void trim(final long n) {
	 final long arrayLength = LongBigArrays.length(a);
	 if (n >= arrayLength || size == arrayLength) return;
	 a = LongBigArrays.trim(a, Math.max(n, size));
	 assert size <= LongBigArrays.length(a);
	}


	   /** Copies element of this type-specific list into the given big array using optimized system calls.
	 *
	 * @param from the start index (inclusive).
	 * @param a the destination big array.
	 * @param offset the offset into the destination array where to store the first element copied.
	 * @param length the number of elements to be copied.
	 */

	@Override
	public void getElements(final long from, final long[][] a, final long offset, final long length) {
	 LongBigArrays.copy(this.a, from, a, offset, length);
	}

	/** Removes elements of this type-specific list using optimized system calls.
	 *
	 * @param from the start index (inclusive).
	 * @param to the end index (exclusive).
	 */
	@Override
	public void removeElements(final long from, final long to) {
	 BigArrays.ensureFromTo(size, from, to);
	 LongBigArrays.copy(a, to, a, from, size - to);
	 size -= (to - from);



	}


	/** Adds elements to this type-specific list using optimized system calls.
	 *
	 * @param index the index at which to add elements.
	 * @param a the big array containing the elements.
	 * @param offset the offset of the first element to add.
	 * @param length the number of elements to add.
	 */
	@Override
	public void addElements(final long index, final long a[][], final long offset, final long length) {
	 ensureIndex(index);
	 LongBigArrays.ensureOffsetLength(a, offset, length);
	 grow(size + length);
	 LongBigArrays.copy(this.a, index, this.a, index + length, size - index);
	 LongBigArrays.copy(a, offset, this.a, index, length);
	 size += length;
	}

	@Override
	public LongBigListIterator listIterator(final long index) {
	 ensureIndex(index);

	 return new LongBigListIterator () {
	   long pos = index, last = -1;

	   @Override
	   public boolean hasNext() { return pos < size; }
	   @Override
	   public boolean hasPrevious() { return pos > 0; }
	   @Override
	   public long nextLong() { if (! hasNext()) throw new NoSuchElementException(); return LongBigArrays.get(a, last = pos++); }
	   @Override
	   public long previousLong() { if (! hasPrevious()) throw new NoSuchElementException(); return LongBigArrays.get(a, last = --pos); }
	   @Override
	   public long nextIndex() { return pos; }
	   @Override
	   public long previousIndex() { return pos - 1; }
	   @Override
	   public void add(long k) {
	    LongBigArrayBigList.this.add(pos++, k);
	    last = -1;
	   }
	   @Override
	   public void set(long k) {
	    if (last == -1) throw new IllegalStateException();
	    LongBigArrayBigList.this.set(last, k);
	   }
	   @Override
	   public void remove() {
	    if (last == -1) throw new IllegalStateException();
	    LongBigArrayBigList.this.removeLong(last);
	    /* If the last operation was a next(), we are removing an element *before* us, and we must decrease pos correspondingly. */
	    if (last < pos) pos--;
	    last = -1;
	   }
	  };
	}

	@Override
	public LongBigArrayBigList clone() {
	 LongBigArrayBigList c = new LongBigArrayBigList (size);
	 LongBigArrays.copy(a, 0, c.a, 0, size);
	 c.size = size;
	 return c;
	}







	/** Compares this type-specific big-array list to another one.
	 *
	 * <p>This method exists only for sake of efficiency. The implementation
	 * inherited from the abstract implementation would already work.
	 *
	 * @param l a type-specific big-array list.
	 * @return true if the argument contains the same elements of this type-specific big-array list.
	 */
	public boolean equals(final LongBigArrayBigList l) {
	 if (l == this) return true;
	 long s = size64();
	 if (s != l.size64()) return false;
	 final long[][] a1 = a;
	 final long[][] a2 = l.a;




	 while(s-- != 0) if (LongBigArrays.get(a1, s) != LongBigArrays.get(a2, s)) return false;

	 return true;
	}




	/** Compares this big list to another big list.
	 *
	 * <p>This method exists only for sake of efficiency. The implementation
	 * inherited from the abstract implementation would already work.
	 *
	 * @param l a big list.
	 * @return a negative integer,
	 * zero, or a positive integer as this big list is lexicographically less than, equal
	 * to, or greater than the argument.
	 */

	public int compareTo(final LongBigArrayBigList l) {
	 final long s1 = size64(), s2 = l.size64();
	 final long a1[][] = a, a2[][] = l.a;
	 long e1, e2;
	 int r, i;

	 for(i = 0; i < s1 && i < s2; i++) {
	  e1 = LongBigArrays.get(a1, i);
	  e2 = LongBigArrays.get(a2, i);
	  if ((r = ( Long.compare((e1),(e2)) )) != 0) return r;
	 }

	 return i < s2 ? -1 : (i < s1 ? 1 : 0);
	}



	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
	 s.defaultWriteObject();
	 for(int i = 0; i < size; i++) s.writeLong(LongBigArrays.get(a, i));
	}


	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
	 s.defaultReadObject();
	 a = LongBigArrays.newBigArray(size);
	 for(int i = 0; i < size; i++) LongBigArrays.set(a, i, s.readLong());
	}
}

