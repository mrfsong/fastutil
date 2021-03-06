
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

import java.util.Collection;
import java.util.SortedSet;

/** A type-specific {@link SortedSet}; provides some additional methods that use polymorphism to avoid (un)boxing.
	*
	* <p>Additionally, this interface strengthens {@link #iterator()},
	* {@link #comparator()} (for primitive types), {@link SortedSet#subSet(Object,Object)},
	* {@link SortedSet#headSet(Object)} and {@link SortedSet#tailSet(Object)}.
	*
	* @see SortedSet
	*/


public interface LongSortedSet extends LongSet , SortedSet<Long>, LongBidirectionalIterable {

	/** Returns a type-specific {@link it.unimi.dsi.fastutil.BidirectionalIterator} on the elements in
	 * this set, starting from a given element of the domain (optional operation).
	 *
	 * <p>This method returns a type-specific bidirectional iterator with given
	 * starting point. The starting point is any element comparable to the
	 * elements of this set (even if it does not actually belong to the
	 * set). The next element of the returned iterator is the least element of
	 * the set that is greater than the starting point (if there are no
	 * elements greater than the starting point, {@link
	 * it.unimi.dsi.fastutil.BidirectionalIterator#hasNext() hasNext()} will return
	 * {@code false}). The previous element of the returned iterator is
	 * the greatest element of the set that is smaller than or equal to the
	 * starting point (if there are no elements smaller than or equal to the
	 * starting point, {@link it.unimi.dsi.fastutil.BidirectionalIterator#hasPrevious()
	 * hasPrevious()} will return {@code false}).
	 *
	 * <p>Note that passing the last element of the set as starting point and
	 * calling {@link it.unimi.dsi.fastutil.BidirectionalIterator#previous() previous()} you can traverse the
	 * entire set in reverse order.
	 *
	 * @param fromElement an element to start from.
	 * @return a bidirectional iterator on the element in this set, starting at the given element.
	 * @throws UnsupportedOperationException if this set does not support iterators with a starting point.
	 */

	LongBidirectionalIterator iterator(long fromElement);

	/** Returns a type-specific {@link it.unimi.dsi.fastutil.BidirectionalIterator} on the elements in
	 * this set.
	 *
	 * <p>Note that this specification strengthens the one given in the corresponding type-specific
	 * {@link Collection}.
	 *
	 * <p>This method returns a parameterised bidirectional iterator. The iterator
	 * can be moreover safely cast to a type-specific iterator.
	 *
	 * @return a bidirectional iterator on the element in this set.
	 */

	@Override
	LongBidirectionalIterator iterator();

	/** Returns a view of the portion of this sorted set whose elements range from {@code fromElement}, inclusive, to {@code toElement}, exclusive.
	 * <p>Note that this specification strengthens the one given in {@link SortedSet#subSet(Object,Object)}.
	 * @see SortedSet#subSet(Object,Object)
	 */




	LongSortedSet subSet(long fromElement, long toElement) ;

	/** Returns a view of the portion of this sorted set whose elements are strictly less than {@code toElement}.
	 * <p>Note that this specification strengthens the one given in {@link SortedSet#headSet(Object)}.
	 * @see SortedSet#headSet(Object)
	 */




	LongSortedSet headSet(long toElement);

	/** Returns a view of the portion of this sorted set whose elements are greater than or equal to {@code fromElement}.
	 * <p>Note that this specification strengthens the one given in {@link SortedSet#headSet(Object)}.
	 * @see SortedSet#tailSet(Object)
	 */




	LongSortedSet tailSet(long fromElement);


	/** {@inheritDoc}
	 * <p>Note that this specification strengthens the one given in {@link SortedSet#comparator()}.
	 */
	@Override
	LongComparator comparator();

	/** Returns the first (lowest) element currently in this set.
	 * @see SortedSet#first()
	 */
	long firstLong();

	/** Returns the last (highest) element currently in this set.
	 * @see SortedSet#last()
	 */
	long lastLong();

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	default LongSortedSet subSet(final Long from, final Long to) {
	 return subSet(from.longValue(), to.longValue());
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	default LongSortedSet headSet(final Long to) {
	 return headSet(to.longValue());
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	default LongSortedSet tailSet(final Long from) {
	 return tailSet(from.longValue());
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	default Long first() {
	 return Long.valueOf(firstLong());
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	default Long last() {
	 return Long.valueOf(lastLong());
	}



}

