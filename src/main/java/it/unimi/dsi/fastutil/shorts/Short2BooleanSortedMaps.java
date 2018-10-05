
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


package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import it.unimi.dsi.fastutil.shorts.Short2BooleanSortedMap.FastSortedEntrySet;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;

/** A class providing static methods and objects that do useful things with type-specific sorted maps.
	*
	* @see java.util.Collections
	*/

public final class Short2BooleanSortedMaps {

	private Short2BooleanSortedMaps() {}

	/** Returns a comparator for entries based on a given comparator on keys.
	 *
	 * @param comparator a comparator on keys.
	 * @return the associated comparator on entries.
	 */
	public static Comparator<? super Map.Entry<Short, ?>> entryComparator(final ShortComparator comparator) {
	 return (Comparator<Map.Entry<Short, ?>>) (x, y) -> comparator.compare((x.getKey()).shortValue(), (y.getKey()).shortValue());
	}

	/** Returns a bidirectional iterator that will be {@linkplain FastSortedEntrySet fast}, if possible, on the {@linkplain Map#entrySet() entry set} of the provided {@code map}.
	 * @param map a map from which we will try to extract a (fast) bidirectional iterator on the entry set.
	 * @return a bidirectional iterator on the entry set of the given map that will be fast, if possible.
	 * @since 8.0.0
	 */

	public static ObjectBidirectionalIterator<Short2BooleanMap.Entry > fastIterator(Short2BooleanSortedMap map) {
	 final ObjectSortedSet<Short2BooleanMap.Entry > entries = map.short2BooleanEntrySet();
	 return entries instanceof Short2BooleanSortedMap.FastSortedEntrySet ? ((Short2BooleanSortedMap.FastSortedEntrySet ) entries).fastIterator() : entries.iterator();
	}

	/** Returns an iterable yielding a bidirectional iterator that will be {@linkplain FastSortedEntrySet fast}, if possible, on the {@linkplain Map#entrySet() entry set} of the provided {@code map}.
	 * @param map a map from which we will try to extract an iterable yielding a (fast) bidirectional iterator on the entry set.
	 * @return an iterable yielding a bidirectional iterator on the entry set of the given map that will be fast, if possible.
	 * @since 8.0.0
	 */

	public static ObjectBidirectionalIterable<Short2BooleanMap.Entry > fastIterable(Short2BooleanSortedMap map) {
	 final ObjectSortedSet<Short2BooleanMap.Entry > entries = map.short2BooleanEntrySet();
	 return entries instanceof Short2BooleanSortedMap.FastSortedEntrySet ? ((Short2BooleanSortedMap.FastSortedEntrySet )entries)::fastIterator : entries;
	}


	/** An immutable class representing an empty type-specific sorted map.
	 *
	 * <p>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

	public static class EmptySortedMap extends Short2BooleanMaps.EmptyMap implements Short2BooleanSortedMap , java.io.Serializable, Cloneable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected EmptySortedMap() {}

	 @Override
	 public ShortComparator comparator() { return null; }

	 @SuppressWarnings("unchecked")
	 @Override
	 public ObjectSortedSet<Short2BooleanMap.Entry > short2BooleanEntrySet() { return ObjectSortedSets.EMPTY_SET; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated



	 @Override
	 @SuppressWarnings("unchecked")
	 public ObjectSortedSet<Map.Entry<Short, Boolean>> entrySet() { return ObjectSortedSets.EMPTY_SET; }


	 @Override
	 public ShortSortedSet keySet() { return ShortSortedSets.EMPTY_SET; }


	 @Override
	 public Short2BooleanSortedMap subMap(final short from, final short to) { return EMPTY_MAP; }


	 @Override
	 public Short2BooleanSortedMap headMap(final short to) { return EMPTY_MAP; }


	 @Override
	 public Short2BooleanSortedMap tailMap(final short from) { return EMPTY_MAP; }

	 @Override
	 public short firstShortKey() { throw new NoSuchElementException(); }

	 @Override
	 public short lastShortKey() { throw new NoSuchElementException(); }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap headMap(Short oto) { return headMap((oto).shortValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap tailMap(Short ofrom) { return tailMap((ofrom).shortValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap subMap(Short ofrom, Short oto) { return subMap((ofrom).shortValue(), (oto).shortValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short firstKey() { return Short.valueOf(firstShortKey()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short lastKey() { return Short.valueOf(lastShortKey()); }

	}



	/** An empty sorted map (immutable). It is serializable and cloneable.
	 */

	public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();
	/** An immutable class representing a type-specific singleton sorted map.
	 *
	 * <p>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

	public static class Singleton extends Short2BooleanMaps.Singleton implements Short2BooleanSortedMap , java.io.Serializable, Cloneable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected final ShortComparator comparator;

	 protected Singleton(final short key, final boolean value, ShortComparator comparator) {
	  super(key, value);
	  this.comparator = comparator;
	 }

	 protected Singleton(final short key, final boolean value) {
	  this(key, value, null);
	 }


	 final int compare(final short k1, final short k2) {
	  return comparator == null ? ( Short.compare((k1),(k2)) ) : comparator.compare(k1, k2);
	 }

	 @Override
	 public ShortComparator comparator() { return comparator; }


	 @Override
	 public ObjectSortedSet<Short2BooleanMap.Entry > short2BooleanEntrySet() { if (entries == null) entries = ObjectSortedSets.singleton(new AbstractShort2BooleanMap.BasicEntry (key, value), entryComparator(comparator)); return (ObjectSortedSet<Short2BooleanMap.Entry >)entries; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated



	 @Override
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	 public ObjectSortedSet<Map.Entry<Short, Boolean>> entrySet() { return (ObjectSortedSet)short2BooleanEntrySet(); }

	 @Override
	 public ShortSortedSet keySet() { if (keys == null) keys = ShortSortedSets.singleton(key, comparator); return (ShortSortedSet )keys; }


	 @Override
	 public Short2BooleanSortedMap subMap(final short from, final short to) { if (compare(from, key) <= 0 && compare(key, to) < 0) return this; return EMPTY_MAP; }


	 @Override
	 public Short2BooleanSortedMap headMap(final short to) { if (compare(key, to) < 0) return this; return EMPTY_MAP; }


	 @Override
	 public Short2BooleanSortedMap tailMap(final short from) { if (compare(from, key) <= 0) return this; return EMPTY_MAP; }

	 @Override
	 public short firstShortKey() { return key; }

	 @Override
	 public short lastShortKey() { return key; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap headMap(Short oto) { return headMap((oto).shortValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap tailMap(Short ofrom) { return tailMap((ofrom).shortValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap subMap(Short ofrom, Short oto) { return subMap((ofrom).shortValue(), (oto).shortValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short firstKey() { return Short.valueOf(firstShortKey()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short lastKey() { return Short.valueOf(lastShortKey()); }

	}

	/** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <p>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair {@code &lt;key,value&gt;}.
	 */

	public static Short2BooleanSortedMap singleton(final Short key, Boolean value) { return new Singleton ((key).shortValue(), (value).booleanValue());}

	/** RETURNS a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <p>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @param comparator the comparator to use in the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair {@code &lt;key,value&gt;}.
	 */

	public static Short2BooleanSortedMap singleton(final Short key, Boolean value, ShortComparator comparator) { return new Singleton ((key).shortValue(), (value).booleanValue(), comparator); }



	/** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <p>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair {@code &lt;key,value&gt;}.
	 */

	public static Short2BooleanSortedMap singleton(final short key, final boolean value) {
	 return new Singleton (key, value);
	}

	/** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <p>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @param comparator the comparator to use in the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair {@code &lt;key,value&gt;}.
	 */

	public static Short2BooleanSortedMap singleton(final short key, final boolean value, ShortComparator comparator) {
	 return new Singleton (key, value, comparator);
	}




	 /** A synchronized wrapper class for sorted maps. */

	public static class SynchronizedSortedMap extends Short2BooleanMaps.SynchronizedMap implements Short2BooleanSortedMap , java.io.Serializable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected final Short2BooleanSortedMap sortedMap;

	 protected SynchronizedSortedMap(final Short2BooleanSortedMap m, final Object sync) {
	  super(m, sync);
	  sortedMap = m;
	 }

	 protected SynchronizedSortedMap(final Short2BooleanSortedMap m) {
	  super(m);
	  sortedMap = m;
	 }

	 @Override
	 public ShortComparator comparator() { synchronized(sync) { return sortedMap.comparator(); } }

	 @Override
	 public ObjectSortedSet<Short2BooleanMap.Entry > short2BooleanEntrySet() { if (entries == null) entries = ObjectSortedSets.synchronize(sortedMap.short2BooleanEntrySet(), sync); return (ObjectSortedSet<Short2BooleanMap.Entry >)entries; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated



	 @Override
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	 public ObjectSortedSet<Map.Entry<Short, Boolean>> entrySet() { return (ObjectSortedSet)short2BooleanEntrySet(); }

	 @Override
	 public ShortSortedSet keySet() { if (keys == null) keys = ShortSortedSets.synchronize(sortedMap.keySet(), sync); return (ShortSortedSet )keys; }

	 @Override
	 public Short2BooleanSortedMap subMap(final short from, final short to) { return new SynchronizedSortedMap (sortedMap.subMap(from, to), sync); }

	 @Override
	 public Short2BooleanSortedMap headMap(final short to) { return new SynchronizedSortedMap (sortedMap.headMap(to), sync); }

	 @Override
	 public Short2BooleanSortedMap tailMap(final short from) { return new SynchronizedSortedMap (sortedMap.tailMap(from), sync); }

	 @Override
	 public short firstShortKey() { synchronized(sync) { return sortedMap.firstShortKey(); } }

	 @Override
	 public short lastShortKey() { synchronized(sync) { return sortedMap.lastShortKey(); } }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short firstKey() { synchronized(sync) { return sortedMap.firstKey(); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short lastKey() { synchronized(sync) { return sortedMap.lastKey(); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap subMap(final Short from, final Short to) { return new SynchronizedSortedMap (sortedMap.subMap(from, to), sync); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap headMap(final Short to) { return new SynchronizedSortedMap (sortedMap.headMap(to), sync); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap tailMap(final Short from) { return new SynchronizedSortedMap (sortedMap.tailMap(from), sync); }



	}

	/** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */
	public static Short2BooleanSortedMap synchronize(final Short2BooleanSortedMap m) { return new SynchronizedSortedMap (m); }

	/** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map, using an assigned object to synchronize.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @param sync an object that will be used to synchronize the access to the sorted sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */

	public static Short2BooleanSortedMap synchronize(final Short2BooleanSortedMap m, final Object sync) { return new SynchronizedSortedMap (m, sync); }




	/** An unmodifiable wrapper class for sorted maps. */

	public static class UnmodifiableSortedMap extends Short2BooleanMaps.UnmodifiableMap implements Short2BooleanSortedMap , java.io.Serializable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected final Short2BooleanSortedMap sortedMap;

	 protected UnmodifiableSortedMap(final Short2BooleanSortedMap m) {
	  super(m);
	  sortedMap = m;
	 }

	 @Override
	 public ShortComparator comparator() { return sortedMap.comparator(); }

	 @Override
	 public ObjectSortedSet<Short2BooleanMap.Entry > short2BooleanEntrySet() { if (entries == null) entries = ObjectSortedSets.unmodifiable(sortedMap.short2BooleanEntrySet()); return (ObjectSortedSet<Short2BooleanMap.Entry >)entries; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated



	 @Override
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	 public ObjectSortedSet<Map.Entry<Short, Boolean>> entrySet() { return (ObjectSortedSet)short2BooleanEntrySet(); }

	 @Override
	 public ShortSortedSet keySet() { if (keys == null) keys = ShortSortedSets.unmodifiable(sortedMap.keySet()); return (ShortSortedSet )keys; }

	 @Override
	 public Short2BooleanSortedMap subMap(final short from, final short to) { return new UnmodifiableSortedMap (sortedMap.subMap(from, to)); }

	 @Override
	 public Short2BooleanSortedMap headMap(final short to) { return new UnmodifiableSortedMap (sortedMap.headMap(to)); }

	 @Override
	 public Short2BooleanSortedMap tailMap(final short from) { return new UnmodifiableSortedMap (sortedMap.tailMap(from)); }

	 @Override
	 public short firstShortKey() { return sortedMap.firstShortKey(); }

	 @Override
	 public short lastShortKey() { return sortedMap.lastShortKey(); }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short firstKey() { return sortedMap.firstKey(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short lastKey() { return sortedMap.lastKey(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap subMap(final Short from, final Short to) { return new UnmodifiableSortedMap (sortedMap.subMap(from, to)); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap headMap(final Short to) { return new UnmodifiableSortedMap (sortedMap.headMap(to)); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Short2BooleanSortedMap tailMap(final Short from) { return new UnmodifiableSortedMap (sortedMap.tailMap(from)); }



	}

	/** Returns an unmodifiable type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in an unmodifiable sorted map.
	 * @return an unmodifiable view of the specified sorted map.
	 * @see java.util.Collections#unmodifiableSortedMap(SortedMap)
	 */
	public static Short2BooleanSortedMap unmodifiable(final Short2BooleanSortedMap m) { return new UnmodifiableSortedMap (m); }
}
