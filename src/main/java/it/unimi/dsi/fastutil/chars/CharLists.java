
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


package it.unimi.dsi.fastutil.chars;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;

/** A class providing static methods and objects that do useful things with type-specific lists.
	*
	* @see java.util.Collections
	*/

public final class CharLists {

	private CharLists() {}

	/** Shuffles the specified list using the specified pseudorandom number generator.
	 *
	 * @param l the list to be shuffled.
	 * @param random a pseudorandom number generator.
	 * @return {@code l}.
	 */
	public static CharList shuffle(final CharList l, final Random random) {
	 for(int i = l.size(); i-- != 0;) {
	  final int p = random.nextInt(i + 1);
	  final char t = l.getChar(i);
	  l.set(i, l.getChar(p));
	  l.set(p, t);
	 }
	 return l;
	}

	/** An immutable class representing an empty type-specific list.
	 *
	 * <p>This class may be useful to implement your own in case you subclass
	 * a type-specific list.
	 */

	public static class EmptyList extends CharCollections.EmptyCollection implements CharList , RandomAccess, java.io.Serializable, Cloneable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected EmptyList() {}

	 @Override
	 public char getChar(int i) { throw new IndexOutOfBoundsException(); }

	 @Override
	 public boolean rem(char k) { throw new UnsupportedOperationException(); }

	 @Override
	 public char removeChar(int i) { throw new UnsupportedOperationException(); }

	 @Override
	 public void add(final int index, final char k) { throw new UnsupportedOperationException(); }

	 @Override
	 public char set(final int index, final char k) { throw new UnsupportedOperationException(); }

	 @Override
	 public int indexOf(char k) { return -1; }

	 @Override
	 public int lastIndexOf(char k) { return -1; }

	 @Override
	 public boolean addAll(int i, Collection<? extends Character> c) { throw new UnsupportedOperationException(); }


	 @Override
	 public boolean addAll(CharList c) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean addAll(int i, CharCollection c) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean addAll(int i, CharList c) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @SuppressWarnings("deprecation")
	 @Deprecated
	 @Override
	 public void add(final int index, final Character k) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @SuppressWarnings("deprecation")
	 @Deprecated
	 @Override
	 public Character get(final int index) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @SuppressWarnings("deprecation")
	 @Deprecated
	 @Override
	 public boolean add(final Character k) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @SuppressWarnings("deprecation")
	 @Deprecated
	 @Override
	 public Character set(final int index, final Character k) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @SuppressWarnings("deprecation")
	 @Deprecated
	 @Override
	 public Character remove(int k) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @SuppressWarnings("deprecation")
	 @Deprecated
	 @Override
	 public int indexOf(Object k) { return -1; }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @SuppressWarnings("deprecation")
	 @Deprecated
	 @Override
	 public int lastIndexOf(Object k) { return -1; }


	 @Override
	 public CharListIterator listIterator() { return CharIterators.EMPTY_ITERATOR; }


	 @Override
	 public CharListIterator iterator() { return CharIterators.EMPTY_ITERATOR; }


	 @Override
	 public CharListIterator listIterator(int i) { if (i == 0) return CharIterators.EMPTY_ITERATOR; throw new IndexOutOfBoundsException(String.valueOf(i)); }

	 @Override
	 public CharList subList(int from, int to) { if (from == 0 && to == 0) return this; throw new IndexOutOfBoundsException(); }

	 @Override
	 public void getElements(int from, char[] a, int offset, int length) { if (from == 0 && length == 0 && offset >= 0 && offset <= a.length) return; throw new IndexOutOfBoundsException(); }

	 @Override
	 public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }

	 @Override
	 public void addElements(int index, final char a[], int offset, int length) { throw new UnsupportedOperationException(); }

	 @Override
	 public void addElements(int index, final char a[]) { throw new UnsupportedOperationException(); }

	 @Override
	 public void size(int s) { throw new UnsupportedOperationException(); }


	 @Override
	 public int compareTo(final List<? extends Character> o) {
	  if (o == this) return 0;
	  return ((List<?>)o).isEmpty() ? 0 : -1;
	 }


	 @Override
	 public Object clone() { return EMPTY_LIST; }

	 @Override
	 public int hashCode() { return 1; }

	 @Override
	 @SuppressWarnings("rawtypes")
	 public boolean equals(Object o) { return o instanceof List && ((List)o).isEmpty(); }

	 @Override
	 public String toString() { return "[]"; }

	 private Object readResolve() { return EMPTY_LIST; }
	}

	/** An empty list (immutable). It is serializable and cloneable.
	 */

	public static final EmptyList EMPTY_LIST = new EmptyList();
	/** An immutable class representing a type-specific singleton list.
	 *
	 * <p>This class may be useful to implement your own in case you subclass
	 * a type-specific list.
	 */

	public static class Singleton extends AbstractCharList implements RandomAccess, java.io.Serializable, Cloneable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 private final char element;

	 protected Singleton(final char element) { this.element = element; }

	 @Override
	 public char getChar(final int i) { if (i == 0) return element; throw new IndexOutOfBoundsException(); }

	 @Override
	 public boolean rem(char k) { throw new UnsupportedOperationException(); }

	 @Override
	 public char removeChar(final int i) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean contains(final char k) { return ( (k) == (element) ); }

	 /* Slightly optimized w.r.t. the one in ABSTRACT_SET. */

	 @Override
	 public char[] toCharArray() {
	  char a[] = new char[1];
	  a[0] = element;
	  return a;
	 }

	 @Override
	 public CharListIterator listIterator() { return CharIterators.singleton(element); }

	 @Override
	 public CharListIterator iterator() { return listIterator(); }

	 @Override
	 public CharListIterator listIterator(final int i) {
	  if (i > 1 || i < 0) throw new IndexOutOfBoundsException();
	  final CharListIterator l = listIterator();
	  if (i == 1) l.nextChar();
	  return l;
	 }

	 @Override

	 public CharList subList(final int from, final int to) {
	  ensureIndex(from);
	  ensureIndex(to);
	  if (from > to) throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");

	  if (from != 0 || to != 1) return EMPTY_LIST;
	  return this;
	 }

	 @Override
	 public boolean addAll(int i, Collection<? extends Character> c) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean addAll(final Collection<? extends Character> c) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean removeAll(final Collection<?> c) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean retainAll(final Collection<?> c) { throw new UnsupportedOperationException(); }



	 @Override
	 public boolean addAll(CharList c) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean addAll(int i, CharList c) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean addAll(int i, CharCollection c) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean addAll(final CharCollection c) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean removeAll(final CharCollection c) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean retainAll(final CharCollection c) { throw new UnsupportedOperationException(); }


	 @Override
	 public int size() { return 1; }

	 @Override
	 public void size(final int size) { throw new UnsupportedOperationException(); }

	 @Override
	 public void clear() { throw new UnsupportedOperationException(); }

	 @Override
	 public Object clone() { return this; }
	}

	/** Returns a type-specific immutable list containing only the specified element. The returned list is serializable and cloneable.
	 *
	 * @param element the only element of the returned list.
	 * @return a type-specific immutable list containing just {@code element}.
	 */

	public static CharList singleton(final char element) { return new Singleton (element); }



	/** Returns a type-specific immutable list containing only the specified element. The returned list is serializable and cloneable.
	 *
	 * @param element the only element of the returned list.
	 * @return a type-specific immutable list containing just {@code element}.
	 */

	public static CharList singleton(final Object element) { return new Singleton (((Character)(element)).charValue()); }




	/** A synchronized wrapper class for lists. */

	public static class SynchronizedList extends CharCollections.SynchronizedCollection implements CharList , java.io.Serializable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected final CharList list; // Due to the large number of methods that are not in COLLECTION, this is worth caching.

	 protected SynchronizedList(final CharList l, final Object sync) {
	  super(l, sync);
	  this.list = l;
	 }

	 protected SynchronizedList(final CharList l) {
	  super(l);
	  this.list = l;
	 }

	 @Override
	 public char getChar(final int i) { synchronized(sync) { return list.getChar(i); } }

	 @Override
	 public char set(final int i, final char k) { synchronized(sync) { return list.set(i, k); } }

	 @Override
	 public void add(final int i, final char k) { synchronized(sync) { list.add(i, k); } }

	 @Override
	 public char removeChar(final int i) { synchronized(sync) { return list.removeChar(i); } }

	 @Override
	 public int indexOf(final char k) { synchronized(sync) { return list.indexOf(k); } }

	 @Override
	 public int lastIndexOf(final char k) { synchronized(sync) { return list.lastIndexOf(k); } }

	 @Override
	 public boolean addAll(final int index, final Collection<? extends Character> c) { synchronized(sync) { return list.addAll(index, c); } }

	 @Override
	 public void getElements(final int from, final char a[], final int offset, final int length) { synchronized(sync) { list.getElements(from, a, offset, length); } }

	 @Override
	 public void removeElements(final int from, final int to) { synchronized(sync) { list.removeElements(from, to); } }

	 @Override
	 public void addElements(int index, final char a[], int offset, int length) { synchronized(sync) { list.addElements(index, a, offset, length); } }

	 @Override
	 public void addElements(int index, final char a[]) { synchronized(sync) { list.addElements(index, a); } }

	 @Override
	 public void size(final int size) { synchronized(sync) { list.size(size); } }

	 @Override
	 public CharListIterator listIterator() { return list.listIterator(); }

	 @Override
	 public CharListIterator iterator() { return listIterator(); }

	 @Override
	 public CharListIterator listIterator(final int i) { return list.listIterator(i); }

	 @Override
	 public CharList subList(final int from, final int to) { synchronized(sync) { return new SynchronizedList (list.subList(from, to), sync); } }

	 @Override
	 public boolean equals(final Object o) { if (o == this) return true; synchronized(sync) { return collection.equals(o); } }

	 @Override
	 public int hashCode() { synchronized(sync) { return collection.hashCode(); } }


	 @Override
	 public int compareTo(final List<? extends Character> o) { synchronized(sync) { return list.compareTo(o); } }



	 @Override
	 public boolean addAll(final int index, final CharCollection c) { synchronized(sync) { return list.addAll(index, c); } }

	 @Override
	 public boolean addAll(final int index, CharList l) { synchronized(sync) { return list.addAll(index, l); } }

	 @Override
	 public boolean addAll(CharList l) { synchronized(sync) { return list.addAll(l); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Character get(final int i) { synchronized(sync) { return list.get(i); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public void add(final int i, Character k) { synchronized(sync) { list.add(i, k); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Character set(final int index, Character k) { synchronized(sync) { return list.set(index, k); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Character remove(final int i) { synchronized(sync) { return list.remove(i); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public int indexOf(final Object o) { synchronized(sync) { return list.indexOf(o); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public int lastIndexOf(final Object o) { synchronized(sync) { return list.lastIndexOf(o); } }


	 private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
	  synchronized(sync) { s.defaultWriteObject(); }
	 }
	}


	/** A synchronized wrapper class for random-access lists. */

	public static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess, java.io.Serializable {
	 private static final long serialVersionUID = 0L;

	 protected SynchronizedRandomAccessList(final CharList l, final Object sync) {
	  super(l, sync);
	 }

	 protected SynchronizedRandomAccessList(final CharList l) {
	  super(l);
	 }

	 @Override
	 public CharList subList(final int from, final int to) { synchronized(sync) { return new SynchronizedRandomAccessList (list.subList(from, to), sync); } }
	}


	/** Returns a synchronized type-specific list backed by the given type-specific list.
	 *
	 * @param l the list to be wrapped in a synchronized list.
	 * @return a synchronized view of the specified list.
	 * @see java.util.Collections#synchronizedList(List)
	 */
	public static CharList synchronize(final CharList l) {
	 return l instanceof RandomAccess ? new SynchronizedRandomAccessList (l) : new SynchronizedList (l);
	}

	/** Returns a synchronized type-specific list backed by the given type-specific list, using an assigned object to synchronize.
	 *
	 * @param l the list to be wrapped in a synchronized list.
	 * @param sync an object that will be used to synchronize the access to the list.
	 * @return a synchronized view of the specified list.
	 * @see java.util.Collections#synchronizedList(List)
	 */

	public static CharList synchronize(final CharList l, final Object sync) {
	 return l instanceof RandomAccess ? new SynchronizedRandomAccessList (l, sync) : new SynchronizedList (l, sync);
	}



	/** An unmodifiable wrapper class for lists. */

	public static class UnmodifiableList extends CharCollections.UnmodifiableCollection implements CharList , java.io.Serializable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected final CharList list; // Due to the large number of methods that are not in COLLECTION, this is worth caching.

	 protected UnmodifiableList(final CharList l) {
	  super(l);
	  this.list = l;
	 }

	 @Override
	 public char getChar(final int i) { return list.getChar(i); }

	 @Override
	 public char set(final int i, final char k) { throw new UnsupportedOperationException(); }

	 @Override
	 public void add(final int i, final char k) { throw new UnsupportedOperationException(); }

	 @Override
	 public char removeChar(final int i) { throw new UnsupportedOperationException(); }

	 @Override
	 public int indexOf(final char k) { return list.indexOf(k); }

	 @Override
	 public int lastIndexOf(final char k) { return list.lastIndexOf(k); }

	 @Override
	 public boolean addAll(final int index, final Collection<? extends Character> c) { throw new UnsupportedOperationException(); }

	 @Override
	 public void getElements(final int from, final char a[], final int offset, final int length) { list.getElements(from, a, offset, length); }

	 @Override
	 public void removeElements(final int from, final int to) { throw new UnsupportedOperationException(); }

	 @Override
	 public void addElements(int index, final char a[], int offset, int length) { throw new UnsupportedOperationException(); }

	 @Override
	 public void addElements(int index, final char a[]) { throw new UnsupportedOperationException(); }

	 @Override
	 public void size(final int size) { list.size(size); }

	 @Override
	 public CharListIterator listIterator() { return CharIterators.unmodifiable(list.listIterator()); }

	 @Override
	 public CharListIterator iterator() { return listIterator(); }

	 @Override
	 public CharListIterator listIterator(final int i) { return CharIterators.unmodifiable(list.listIterator(i)); }

	 @Override
	 public CharList subList(final int from, final int to) { return new UnmodifiableList (list.subList(from, to)); }

	 @Override
	 public boolean equals(final Object o) { if (o == this) return true; return collection.equals(o); }

	 @Override
	 public int hashCode() { return collection.hashCode(); }


	 @Override
	 public int compareTo(final List<? extends Character> o) { return list.compareTo(o); }



	 @Override
	 public boolean addAll(final int index, final CharCollection c) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean addAll(final CharList l) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean addAll(final int index, final CharList l) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Character get(final int i) { return list.get(i); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public void add(final int i, Character k) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Character set(final int index, Character k) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Character remove(final int i) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public int indexOf(final Object o) { return list.indexOf(o); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public int lastIndexOf(final Object o) { return list.lastIndexOf(o); }

	}


	/** An unmodifiable wrapper class for random-access lists. */

	public static class UnmodifiableRandomAccessList extends UnmodifiableList implements RandomAccess, java.io.Serializable {

	 private static final long serialVersionUID = 0L;

	 protected UnmodifiableRandomAccessList(final CharList l) {
	  super(l);
	 }


	 @Override
	 public CharList subList(final int from, final int to) { return new UnmodifiableRandomAccessList (list.subList(from, to)); }
	}


	/** Returns an unmodifiable type-specific list backed by the given type-specific list.
	 *
	 * @param l the list to be wrapped in an unmodifiable list.
	 * @return an unmodifiable view of the specified list.
	 * @see java.util.Collections#unmodifiableList(List)
	 */
	public static CharList unmodifiable(final CharList l) {
	 return l instanceof RandomAccess ? new UnmodifiableRandomAccessList (l) : new UnmodifiableList (l);
	}
}

