
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

import java.util.Iterator;
import java.util.NoSuchElementException;

/** A class providing static methods and objects that do useful things with type-specific iterators.
	*
	* @see Iterator
	*/

public final class LongBigListIterators {

	private LongBigListIterators() {}

	/** A class returning no elements and a type-specific big list iterator interface.
	 *
	 * <p>This class may be useful to implement your own in case you subclass
	 * a type-specific iterator.
	 */

	public static class EmptyBigListIterator implements LongBigListIterator , java.io.Serializable, Cloneable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected EmptyBigListIterator() {}

	 @Override
	 public boolean hasNext() { return false; }

	 @Override
	 public boolean hasPrevious() { return false; }

	 @Override
	 public long nextLong() { throw new NoSuchElementException(); }

	 @Override
	 public long previousLong() { throw new NoSuchElementException(); }

	 @Override
	 public long nextIndex() { return 0; }

	 @Override
	 public long previousIndex() { return -1; }

	 @Override
	 public long skip(long n) { return 0; };

	 @Override
	 public long back(long n) { return 0; };

	 @Override
	 public Object clone() { return EMPTY_BIG_LIST_ITERATOR; }

	 private Object readResolve() { return EMPTY_BIG_LIST_ITERATOR; }
	}

	/** An empty iterator (immutable). It is serializable and cloneable.
	 *
	 * <p>The class of this objects represent an abstract empty iterator
	 * that can iterate as a type-specific (list) iterator.
	 */


	public static final EmptyBigListIterator EMPTY_BIG_LIST_ITERATOR = new EmptyBigListIterator();


	/** An iterator returning a single element. */

	private static class SingletonBigListIterator implements LongBigListIterator {
	 private final long element;
	 private int curr;

	 public SingletonBigListIterator(final long element) {
	  this.element = element;
	 }

	 @Override
	 public boolean hasNext() { return curr == 0; }

	 @Override
	 public boolean hasPrevious() { return curr == 1; }

	 @Override
	 public long nextLong() {
	  if (! hasNext()) throw new NoSuchElementException();
	  curr = 1;
	  return element;
	 }

	 @Override
	 public long previousLong() {
	  if (! hasPrevious()) throw new NoSuchElementException();
	  curr = 0;
	  return element;
	 }

	 @Override
	 public long nextIndex() {
	  return curr;
	 }

	 @Override
	 public long previousIndex() {
	  return curr - 1;
	 }
	}


	/** Returns an iterator that iterates just over the given element.
	 *
	 * @param element the only element to be returned by a type-specific list iterator.
	 * @return  an iterator that iterates just over {@code element}.
	 */
	public static LongBigListIterator singleton(final long element) {
	 return new SingletonBigListIterator (element);
	}


	  /** An unmodifiable wrapper class for big list iterators. */

	public static class UnmodifiableBigListIterator implements LongBigListIterator {
	 protected final LongBigListIterator i;

	 public UnmodifiableBigListIterator(final LongBigListIterator i) {
	  this.i = i;
	 }

	 @Override
	 public boolean hasNext() { return i.hasNext(); }

	 @Override
	 public boolean hasPrevious() { return i.hasPrevious(); }

	 @Override
	 public long nextLong() { return i.nextLong(); }

	 @Override
	 public long previousLong() { return i.previousLong(); }

	 @Override
	 public long nextIndex() { return i.nextIndex(); }

	 @Override
	 public long previousIndex() { return i.previousIndex(); }
	}

	/** Returns an unmodifiable list iterator backed by the specified list iterator.
	 *
	 * @param i the list iterator to be wrapped in an unmodifiable list iterator.
	 * @return an unmodifiable view of the specified list iterator.
	 */
	public static LongBigListIterator unmodifiable(final LongBigListIterator i) { return new UnmodifiableBigListIterator (i); }


	/** A class exposing a list iterator as a big-list iterator.. */

	public static class BigListIteratorListIterator implements LongBigListIterator {
	 protected final LongListIterator i;

	 protected BigListIteratorListIterator(final LongListIterator i) {
	  this.i = i;
	 }

	 private int intDisplacement(long n) {
	  if (n < Integer.MIN_VALUE || n > Integer.MAX_VALUE) throw new IndexOutOfBoundsException("This big iterator is restricted to 32-bit displacements");
	  return (int)n;
	 }

	 @Override
	 public void set(long ok) { i.set(ok); }

	 @Override
	 public void add(long ok) { i.add(ok); }

	 @Override
	 public int back(int n) { return i.back(n); }

	 @Override
	 public long back(long n) { return i.back(intDisplacement(n)); }

	 @Override
	 public void remove() { i.remove(); }

	 @Override
	 public int skip(int n) { return i.skip(n); }

	 @Override
	 public long skip(long n) { return i.skip(intDisplacement(n)); }

	 @Override
	 public boolean hasNext() { return i.hasNext(); }

	 @Override
	 public boolean hasPrevious() { return i.hasPrevious(); }

	 @Override
	 public long nextLong() { return i.nextLong(); }

	 @Override
	 public long previousLong() { return i.previousLong(); }

	 @Override
	 public long nextIndex() { return i.nextIndex(); }

	 @Override
	 public long previousIndex() { return i.previousIndex(); }
	}

	 /** Returns a big-list iterator backed by the specified list iterator.
	  *
	  * @param i the list iterator to adapted to the big-list-iterator interface.
	  * @return a big-list iterator backed by the specified list iterator.
	  */
	public static LongBigListIterator asBigListIterator(final LongListIterator i) { return new BigListIteratorListIterator (i); }
}

