
/*
	* Copyright (C) 2010-2017 Sebastiano Vigna
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


import it.unimi.dsi.fastutil.BigList;
import it.unimi.dsi.fastutil.BigListIterator;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**  An abstract class providing basic methods for big lists implementing a type-specific big list interface. */

public abstract class AbstractLongBigList extends AbstractLongCollection implements LongBigList , LongStack {

	protected AbstractLongBigList() {}

	/** Ensures that the given index is nonnegative and not greater than this big-list size.
	 *
	 * @param index an index.
	 * @throws IndexOutOfBoundsException if the given index is negative or greater than this big-list size.
	 */
	protected void ensureIndex(final long index) {
	 if (index < 0) throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
	 if (index > size64()) throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + (size64()) + ")");
	}

	/** Ensures that the given index is nonnegative and smaller than this big-list size.
	 *
	 * @param index an index.
	 * @throws IndexOutOfBoundsException if the given index is negative or not smaller than this big-list size.
	 */
	protected void ensureRestrictedIndex(final long index) {
	 if (index < 0) throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
	 if (index >= size64()) throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + (size64()) + ")");
	}

	/** {@inheritDoc}
	 *
	 * <p>This implementation always throws an {@link UnsupportedOperationException}.
	 */
	@Override
	public void add(final long index, final long k) {
	 throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the type-specific version of {@link BigList#add(long, Object)}.
	 */
	@Override
	public boolean add(final long k) {
	 add(size64(), k);
	 return true;
	}

	/** {@inheritDoc}
	 *
	 * <p>This implementation always throws an {@link UnsupportedOperationException}.
	 */
	@Override
	public long removeLong(long i) {
	 throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 *
	 * <p>This implementation always throws an {@link UnsupportedOperationException}.
	 */
	@Override
	public long set(final long index, final long k) {
	 throw new UnsupportedOperationException();
	}

	/** Adds all of the elements in the specified collection to this list (optional operation). */
	@Override
	public boolean addAll(long index, final Collection<? extends Long> c) {
	 ensureIndex(index);
	 final Iterator<? extends Long> i = c.iterator();
	 final boolean retVal = i.hasNext();
	 while(i.hasNext()) add(index++, i.next());
	 return retVal;
	}

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the type-specific version of {@link BigList#addAll(long, Collection)}.
	 */
	@Override
	public boolean addAll(final Collection<? extends Long> c) {
	 return addAll(size64(), c);
	}

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to {@link #listIterator()}.
	 */
	@Override
	public LongBigListIterator iterator() {
	 return listIterator();
	}

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to {@link BigList#listIterator(long) listIterator(0)}.
	 */
	@Override
	public LongBigListIterator listIterator() {
	 return listIterator(0L);
	}

	/** {@inheritDoc}
	 * <p>This implementation is based on the random-access methods. */
	@Override
	public LongBigListIterator listIterator(final long index) {
	 ensureIndex(index);

	 return new LongBigListIterator () {
	   long pos = index, last = -1;

	   @Override
	   public boolean hasNext() { return pos < AbstractLongBigList.this.size64(); }

	   @Override
	   public boolean hasPrevious() { return pos > 0; }

	   @Override
	   public long nextLong() { if (! hasNext()) throw new NoSuchElementException(); return AbstractLongBigList.this.getLong(last = pos++); }

	   @Override
	   public long previousLong() { if (! hasPrevious()) throw new NoSuchElementException(); return AbstractLongBigList.this.getLong(last = --pos); }

	   @Override
	   public long nextIndex() { return pos; }

	   @Override
	   public long previousIndex() { return pos - 1; }

	   @Override
	   public void add(long k) {
	    AbstractLongBigList.this.add(pos++, k);
	    last = -1;
	   }

	   @Override
	   public void set(long k) {
	    if (last == -1) throw new IllegalStateException();
	    AbstractLongBigList.this.set(last, k);
	   }

	   @Override
	   public void remove() {
	    if (last == -1) throw new IllegalStateException();
	    AbstractLongBigList.this.removeLong(last);
	    /* If the last operation was a next(), we are removing an element *before* us, and we must decrease pos correspondingly. */
	    if (last < pos) pos--;
	    last = -1;
	   }
	  };
	}


	/** Returns true if this list contains the specified element.
	 * <p>This implementation delegates to {@code indexOf()}.
	 * @see BigList#contains(Object)
	 */
	@Override
	public boolean contains(final long k) { return indexOf(k) >= 0; }

	@Override
	public long indexOf(final long k) {
	 final LongBigListIterator i = listIterator();
	 long e;
	 while(i.hasNext()) {
	  e = i.nextLong();
	  if (( (k) == (e) )) return i.previousIndex();
	 }
	 return -1;
	}

	@Override
	public long lastIndexOf(final long k) {
	 LongBigListIterator i = listIterator(size64());
	 long e;
	 while(i.hasPrevious()) {
	  e = i.previousLong();
	  if (( (k) == (e) )) return i.nextIndex();
	 }
	 return -1;
	}

	@Override
	public void size(final long size) {
	 long i = size64();
	 if (size > i) while(i++ < size) add((0));
	 else while(i-- != size) remove(i);
	}

	@Override
	public LongBigList subList(final long from, final long to) {
	 ensureIndex(from);
	 ensureIndex(to);
	 if (from > to) throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");

	 return new LongSubList (this, from, to);
	}

	/** {@inheritDoc}
	 *
	 * <p>This is a trivial iterator-based implementation. It is expected that
	 * implementations will override this method with a more optimized version.
	 */
	@Override
	public void removeElements(final long from, final long to) {
	 ensureIndex(to);
	 LongBigListIterator i = listIterator(from);
	 long n = to - from;
	 if (n < 0) throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
	 while(n-- != 0) {
	  i.nextLong();
	  i.remove();
	 }
	}

	/** {@inheritDoc}
	 *
	 * <p>This is a trivial iterator-based implementation. It is expected that
	 * implementations will override this method with a more optimized version.
	 */
	@Override
	public void addElements(long index, final long a[][], long offset, long length) {
	 ensureIndex(index);
	 LongBigArrays.ensureOffsetLength(a, offset, length);
	 while(length-- != 0) add(index++, LongBigArrays.get(a, offset++));
	}

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the analogous method for big-array fragments.
	 */
	@Override
	public void addElements(final long index, final long a[][]) {
	 addElements(index, a, 0, LongBigArrays.length(a));
	}

	/** {@inheritDoc}
	 *
	 * <p>This is a trivial iterator-based implementation. It is expected that
	 * implementations will override this method with a more optimized version.
	 */
	@Override
	public void getElements(final long from, final long a[][], long offset, long length) {
	 LongBigListIterator i = listIterator(from);
	 LongBigArrays.ensureOffsetLength(a, offset, length);
	 if (from + length > size64()) throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + size64() + ")");
	 while(length-- != 0) LongBigArrays.set(a, offset++, i.nextLong());
	}

	/** {@inheritDoc}
	 * <p>This implementation delegates to {@link #removeElements(long, long)}.*/
	@Override
	public void clear() {
	 removeElements(0, size64());
	}

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to {@link #size64()}.
	 * @deprecated Please use {@link #size64()} instead. */
	@Override
	@Deprecated
	public int size() {
	 return (int)Math.min(Integer.MAX_VALUE, size64());
	}


	private boolean valEquals(final Object a, final Object b) { return a == null ? b == null : a.equals(b); }


	/** Returns the hash code for this big list, which is identical to {@link java.util.List#hashCode()}.
	 *
	 * @return the hash code for this big list.
	 */
	@Override
	public int hashCode() {
	 LongIterator i = iterator();
	 int h = 1;
	 long s = size64();
	 while (s-- != 0) {
	  long k = i.nextLong();
	  h = 31 * h + it.unimi.dsi.fastutil.HashCommon.long2int(k);
	 }
	 return h;
	}

	@Override
	public boolean equals(final Object o) {
	 if (o == this) return true;
	 if (! (o instanceof BigList)) return false;
	 final BigList<?> l = (BigList<?>)o;
	 long s = size64();
	 if (s != l.size64()) return false;


	 if (l instanceof LongBigList) {
	  final LongBigListIterator i1 = listIterator(), i2 = ((LongBigList )l).listIterator();
	  while(s-- != 0) if (i1.nextLong() != i2.nextLong()) return false;
	  return true;
	 }


	 final BigListIterator<?> i1 = listIterator(), i2 = l.listIterator();




	 while(s-- != 0) if (! valEquals(i1.next(), i2.next())) return false;

	 return true;
	}


	/** Compares this big list to another object. If the
	 * argument is a {@link BigList}, this method performs a lexicographical comparison; otherwise,
	 * it throws a {@code ClassCastException}.
	 *
	 * @param l a big list.
	 * @return if the argument is a {@link BigList}, a negative integer,
	 * zero, or a positive integer as this list is lexicographically less than, equal
	 * to, or greater than the argument.
	 * @throws ClassCastException if the argument is not a big list.
	 */


	@Override
	public int compareTo(final BigList<? extends Long> l) {
	 if (l == this) return 0;

	 if (l instanceof LongBigList) {

	  final LongBigListIterator i1 = listIterator(), i2 = ((LongBigList )l).listIterator();
	  int r;
	  long e1, e2;

	  while(i1.hasNext() && i2.hasNext()) {
	   e1 = i1.nextLong();
	   e2 = i2.nextLong();
	   if ((r = ( Long.compare((e1),(e2)) )) != 0) return r;
	  }
	  return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
	 }

	 BigListIterator<? extends Long> i1 = listIterator(), i2 = l.listIterator();
	 int r;

	 while(i1.hasNext() && i2.hasNext()) {
	  if ((r = ((Comparable<? super Long>)i1.next()).compareTo(i2.next())) != 0) return r;
	 }
	 return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
	}


	@Override
	public void push(long o) {
	 add(o);
	}

	@Override
	public long popLong() {
	 if (isEmpty()) throw new NoSuchElementException();
	 return removeLong(size64() - 1);
	}

	@Override
	public long topLong() {
	 if (isEmpty()) throw new NoSuchElementException();
	 return getLong(size64() - 1);
	}

	@Override
	public long peekLong(int i) {
	 return getLong(size64() - 1 - i);
	}



	/** Removes a single instance of the specified element from this collection, if it is present (optional operation).
	 * <p>This implementation delegates to {@code indexOf()}.
	 * @see BigList#remove(Object)
	 */
	@Override
	public boolean rem(long k) {
	 long index = indexOf(k);
	 if (index == -1) return false;
	 removeLong(index);
	 return true;
	}

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the type-specific version of {@link #addAll(long, Collection)}.
	 */
	@Override
	public boolean addAll(final long index, final LongCollection c) { return addAll(index, (Collection<? extends Long>)c); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the type-specific version of {@link #addAll(long, Collection)}.
	 */
	@Override
	public boolean addAll(final long index, final LongBigList l) { return addAll(index, (LongCollection)l); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the type-specific version of {@link #addAll(long, Collection)}.
	 */
	@Override
	public boolean addAll(final LongCollection c) { return addAll(size64(), c); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the type-specific list version of {@link #addAll(long, Collection)}.
	 */
	@Override
	public boolean addAll(final LongBigList l) { return addAll(size64(), l); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the corresponding type-specific method.
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	public void add(final long index, final Long ok) { add(index, ok.longValue()); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the corresponding type-specific method.
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	public Long set(final long index, final Long ok) { return Long.valueOf(set(index, ok.longValue())); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the corresponding type-specific method.
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	public Long get(final long index) { return Long.valueOf(getLong(index)); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the corresponding type-specific method.
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	public long indexOf(final Object ok) { return indexOf(((Long)(ok)).longValue()); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the corresponding type-specific method.
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	public long lastIndexOf(final Object ok) { return lastIndexOf(((Long)(ok)).longValue()); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the corresponding type-specific method.
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	public Long remove(final long index) { return Long.valueOf(removeLong(index)); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the corresponding type-specific method.
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	public void push(Long o) { push(o.longValue()); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the corresponding type-specific method.
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	public Long pop() { return Long.valueOf(popLong()); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the corresponding type-specific method.
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	public Long top() { return Long.valueOf(topLong()); }

	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the corresponding type-specific method.
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	public Long peek(int i) { return Long.valueOf(peekLong(i)); }



	@Override
	public String toString() {
	 final StringBuilder s = new StringBuilder();
	 final LongIterator i = iterator();
	 long n = size64();
	 long k;
	 boolean first = true;

	 s.append("[");

	 while(n-- != 0) {
	  if (first) first = false;
	  else s.append(", ");
	  k = i.nextLong();



	   s.append(String.valueOf(k));
	 }

	 s.append("]");
	 return s.toString();
	}


	/** A class implementing a sublist view. */
	public static class LongSubList extends AbstractLongBigList implements java.io.Serializable {
	 private static final long serialVersionUID = -7046029254386353129L;
	 /** The list this sublist restricts. */
	 protected final LongBigList l;
	 /** Initial (inclusive) index of this sublist. */
	 protected final long from;
	 /** Final (exclusive) index of this sublist. */
	 protected long to;

	 public LongSubList(final LongBigList l, final long from, final long to) {
	  this.l = l;
	  this.from = from;
	  this.to = to;
	 }

	 private boolean assertRange() {
	  assert from <= l.size64();
	  assert to <= l.size64();
	  assert to >= from;
	  return true;
	 }

	 @Override
	 public boolean add(final long k) {
	  l.add(to, k);
	  to++;
	  assert assertRange();
	  return true;
	 }

	 @Override
	 public void add(final long index, final long k) {
	  ensureIndex(index);
	  l.add(from + index, k);
	  to++;
	  assert assertRange();
	 }

	 @Override
	 public boolean addAll(final long index, final Collection<? extends Long> c) {
	  ensureIndex(index);
	  to += c.size();
	  return l.addAll(from + index, c);
	 }

	 @Override
	 public long getLong(long index) {
	  ensureRestrictedIndex(index);
	  return l.getLong(from + index);
	 }

	 @Override
	 public long removeLong(long index) {
	  ensureRestrictedIndex(index);
	  to--;
	  return l.removeLong(from + index);
	 }

	 @Override
	 public long set(long index, long k) {
	  ensureRestrictedIndex(index);
	  return l.set(from + index, k);
	 }

	 @Override
	 public long size64() { return to - from; }

	 @Override
	 public void getElements(final long from, final long[][] a, final long offset, final long length) {
	  ensureIndex(from);
	  if (from + length > size64()) throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + size64() + ")");
	  l.getElements(this.from + from, a, offset, length);
	 }

	 @Override
	 public void removeElements(final long from, final long to) {
	  ensureIndex(from);
	  ensureIndex(to);
	  l.removeElements(this.from + from, this.from + to);
	  this.to -= (to - from);
	  assert assertRange();
	 }

	 @Override
	 public void addElements(final long index, final long a[][], long offset, long length) {
	  ensureIndex(index);
	  l.addElements(this.from + index, a, offset, length);
	  this.to += length;
	  assert assertRange();
	 }

	 @Override
	 public LongBigListIterator listIterator(final long index) {
	  ensureIndex(index);

	  return new LongBigListIterator () {
	   long pos = index, last = -1;

	   @Override
	   public boolean hasNext() { return pos < size64(); }
	   @Override
	   public boolean hasPrevious() { return pos > 0; }
	   @Override
	   public long nextLong() { if (! hasNext()) throw new NoSuchElementException(); return l.getLong(from + (last = pos++)); }
	   @Override
	   public long previousLong() { if (! hasPrevious()) throw new NoSuchElementException(); return l.getLong(from + (last = --pos)); }
	   @Override
	   public long nextIndex() { return pos; }
	   @Override
	   public long previousIndex() { return pos - 1; }
	   @Override
	   public void add(long k) {
	    if (last == -1) throw new IllegalStateException();
	    LongSubList.this.add(pos++, k);
	    last = -1;
	    assert assertRange();
	   }
	   @Override
	   public void set(long k) {
	    if (last == -1) throw new IllegalStateException();
	    LongSubList.this.set(last, k);
	   }
	   @Override
	   public void remove() {
	    if (last == -1) throw new IllegalStateException();
	    LongSubList.this.removeLong(last);
	    /* If the last operation was a next(), we are removing an element *before* us, and we must decrease pos correspondingly. */
	    if (last < pos) pos--;
	    last = -1;
	    assert assertRange();
	   }
	  };
	 }

	 @Override
	 public LongBigList subList(final long from, final long to) {
	  ensureIndex(from);
	  ensureIndex(to);
	  if (from > to) throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");

	  return new LongSubList (this, from, to);
	 }


	 @Override
	 public boolean rem(long k) {
	  long index = indexOf(k);
	  if (index == -1) return false;
	  to--;
	  l.removeLong(from + index);
	  assert assertRange();
	  return true;
	 }

	 @Override
	 public boolean addAll(final long index, final LongCollection c) {
	  ensureIndex(index);
	  return super.addAll(index, c);
	 }

	 @Override
	 public boolean addAll(final long index, final LongBigList l) {
	  ensureIndex(index);
	  return super.addAll(index, l);
	 }

	}
}

