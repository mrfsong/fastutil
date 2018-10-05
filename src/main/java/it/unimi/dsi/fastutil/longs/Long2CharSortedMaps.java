
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

import it.unimi.dsi.fastutil.longs.Long2CharSortedMap.FastSortedEntrySet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;

/** A class providing static methods and objects that do useful things with type-specific sorted maps.
	*
	* @see java.util.Collections
	*/

public final class Long2CharSortedMaps {

	private Long2CharSortedMaps() {}

	/** Returns a comparator for entries based on a given comparator on keys.
	 *
	 * @param comparator a comparator on keys.
	 * @return the associated comparator on entries.
	 */
	public static Comparator<? super Map.Entry<Long, ?>> entryComparator(final LongComparator comparator) {
	 return (Comparator<Map.Entry<Long, ?>>) (x, y) -> comparator.compare((x.getKey()).longValue(), (y.getKey()).longValue());
	}

	/** Returns a bidirectional iterator that will be {@linkplain FastSortedEntrySet fast}, if possible, on the {@linkplain Map#entrySet() entry set} of the provided {@code map}.
	 * @param map a map from which we will try to extract a (fast) bidirectional iterator on the entry set.
	 * @return a bidirectional iterator on the entry set of the given map that will be fast, if possible.
	 * @since 8.0.0
	 */

	public static ObjectBidirectionalIterator<Long2CharMap.Entry > fastIterator(Long2CharSortedMap map) {
	 final ObjectSortedSet<Long2CharMap.Entry > entries = map.long2CharEntrySet();
	 return entries instanceof Long2CharSortedMap.FastSortedEntrySet ? ((Long2CharSortedMap.FastSortedEntrySet ) entries).fastIterator() : entries.iterator();
	}

	/** Returns an iterable yielding a bidirectional iterator that will be {@linkplain FastSortedEntrySet fast}, if possible, on the {@linkplain Map#entrySet() entry set} of the provided {@code map}.
	 * @param map a map from which we will try to extract an iterable yielding a (fast) bidirectional iterator on the entry set.
	 * @return an iterable yielding a bidirectional iterator on the entry set of the given map that will be fast, if possible.
	 * @since 8.0.0
	 */

	public static ObjectBidirectionalIterable<Long2CharMap.Entry > fastIterable(Long2CharSortedMap map) {
	 final ObjectSortedSet<Long2CharMap.Entry > entries = map.long2CharEntrySet();
	 return entries instanceof Long2CharSortedMap.FastSortedEntrySet ? ((Long2CharSortedMap.FastSortedEntrySet )entries)::fastIterator : entries;
	}


	/** An immutable class representing an empty type-specific sorted map.
	 *
	 * <p>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

	public static class EmptySortedMap extends Long2CharMaps.EmptyMap implements Long2CharSortedMap , java.io.Serializable, Cloneable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected EmptySortedMap() {}

	 @Override
	 public LongComparator comparator() { return null; }

	 @SuppressWarnings("unchecked")
	 @Override
	 public ObjectSortedSet<Long2CharMap.Entry > long2CharEntrySet() { return ObjectSortedSets.EMPTY_SET; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated



	 @Override
	 @SuppressWarnings("unchecked")
	 public ObjectSortedSet<Map.Entry<Long, Character>> entrySet() { return ObjectSortedSets.EMPTY_SET; }


	 @Override
	 public LongSortedSet keySet() { return LongSortedSets.EMPTY_SET; }


	 @Override
	 public Long2CharSortedMap subMap(final long from, final long to) { return EMPTY_MAP; }


	 @Override
	 public Long2CharSortedMap headMap(final long to) { return EMPTY_MAP; }


	 @Override
	 public Long2CharSortedMap tailMap(final long from) { return EMPTY_MAP; }

	 @Override
	 public long firstLongKey() { throw new NoSuchElementException(); }

	 @Override
	 public long lastLongKey() { throw new NoSuchElementException(); }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap headMap(Long oto) { return headMap((oto).longValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap tailMap(Long ofrom) { return tailMap((ofrom).longValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap subMap(Long ofrom, Long oto) { return subMap((ofrom).longValue(), (oto).longValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long firstKey() { return Long.valueOf(firstLongKey()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long lastKey() { return Long.valueOf(lastLongKey()); }

	}



	/** An empty sorted map (immutable). It is serializable and cloneable.
	 */

	public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();
	/** An immutable class representing a type-specific singleton sorted map.
	 *
	 * <p>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

	public static class Singleton extends Long2CharMaps.Singleton implements Long2CharSortedMap , java.io.Serializable, Cloneable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected final LongComparator comparator;

	 protected Singleton(final long key, final char value, LongComparator comparator) {
	  super(key, value);
	  this.comparator = comparator;
	 }

	 protected Singleton(final long key, final char value) {
	  this(key, value, null);
	 }


	 final int compare(final long k1, final long k2) {
	  return comparator == null ? ( Long.compare((k1),(k2)) ) : comparator.compare(k1, k2);
	 }

	 @Override
	 public LongComparator comparator() { return comparator; }


	 @Override
	 public ObjectSortedSet<Long2CharMap.Entry > long2CharEntrySet() { if (entries == null) entries = ObjectSortedSets.singleton(new AbstractLong2CharMap.BasicEntry (key, value), entryComparator(comparator)); return (ObjectSortedSet<Long2CharMap.Entry >)entries; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated



	 @Override
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	 public ObjectSortedSet<Map.Entry<Long, Character>> entrySet() { return (ObjectSortedSet)long2CharEntrySet(); }

	 @Override
	 public LongSortedSet keySet() { if (keys == null) keys = LongSortedSets.singleton(key, comparator); return (LongSortedSet )keys; }


	 @Override
	 public Long2CharSortedMap subMap(final long from, final long to) { if (compare(from, key) <= 0 && compare(key, to) < 0) return this; return EMPTY_MAP; }


	 @Override
	 public Long2CharSortedMap headMap(final long to) { if (compare(key, to) < 0) return this; return EMPTY_MAP; }


	 @Override
	 public Long2CharSortedMap tailMap(final long from) { if (compare(from, key) <= 0) return this; return EMPTY_MAP; }

	 @Override
	 public long firstLongKey() { return key; }

	 @Override
	 public long lastLongKey() { return key; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap headMap(Long oto) { return headMap((oto).longValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap tailMap(Long ofrom) { return tailMap((ofrom).longValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap subMap(Long ofrom, Long oto) { return subMap((ofrom).longValue(), (oto).longValue()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long firstKey() { return Long.valueOf(firstLongKey()); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long lastKey() { return Long.valueOf(lastLongKey()); }

	}

	/** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <p>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair {@code &lt;key,value&gt;}.
	 */

	public static Long2CharSortedMap singleton(final Long key, Character value) { return new Singleton ((key).longValue(), (value).charValue());}

	/** RETURNS a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <p>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @param comparator the comparator to use in the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair {@code &lt;key,value&gt;}.
	 */

	public static Long2CharSortedMap singleton(final Long key, Character value, LongComparator comparator) { return new Singleton ((key).longValue(), (value).charValue(), comparator); }



	/** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <p>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair {@code &lt;key,value&gt;}.
	 */

	public static Long2CharSortedMap singleton(final long key, final char value) {
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

	public static Long2CharSortedMap singleton(final long key, final char value, LongComparator comparator) {
	 return new Singleton (key, value, comparator);
	}




	 /** A synchronized wrapper class for sorted maps. */

	public static class SynchronizedSortedMap extends Long2CharMaps.SynchronizedMap implements Long2CharSortedMap , java.io.Serializable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected final Long2CharSortedMap sortedMap;

	 protected SynchronizedSortedMap(final Long2CharSortedMap m, final Object sync) {
	  super(m, sync);
	  sortedMap = m;
	 }

	 protected SynchronizedSortedMap(final Long2CharSortedMap m) {
	  super(m);
	  sortedMap = m;
	 }

	 @Override
	 public LongComparator comparator() { synchronized(sync) { return sortedMap.comparator(); } }

	 @Override
	 public ObjectSortedSet<Long2CharMap.Entry > long2CharEntrySet() { if (entries == null) entries = ObjectSortedSets.synchronize(sortedMap.long2CharEntrySet(), sync); return (ObjectSortedSet<Long2CharMap.Entry >)entries; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated



	 @Override
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	 public ObjectSortedSet<Map.Entry<Long, Character>> entrySet() { return (ObjectSortedSet)long2CharEntrySet(); }

	 @Override
	 public LongSortedSet keySet() { if (keys == null) keys = LongSortedSets.synchronize(sortedMap.keySet(), sync); return (LongSortedSet )keys; }

	 @Override
	 public Long2CharSortedMap subMap(final long from, final long to) { return new SynchronizedSortedMap (sortedMap.subMap(from, to), sync); }

	 @Override
	 public Long2CharSortedMap headMap(final long to) { return new SynchronizedSortedMap (sortedMap.headMap(to), sync); }

	 @Override
	 public Long2CharSortedMap tailMap(final long from) { return new SynchronizedSortedMap (sortedMap.tailMap(from), sync); }

	 @Override
	 public long firstLongKey() { synchronized(sync) { return sortedMap.firstLongKey(); } }

	 @Override
	 public long lastLongKey() { synchronized(sync) { return sortedMap.lastLongKey(); } }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long firstKey() { synchronized(sync) { return sortedMap.firstKey(); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long lastKey() { synchronized(sync) { return sortedMap.lastKey(); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap subMap(final Long from, final Long to) { return new SynchronizedSortedMap (sortedMap.subMap(from, to), sync); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap headMap(final Long to) { return new SynchronizedSortedMap (sortedMap.headMap(to), sync); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap tailMap(final Long from) { return new SynchronizedSortedMap (sortedMap.tailMap(from), sync); }



	}

	/** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */
	public static Long2CharSortedMap synchronize(final Long2CharSortedMap m) { return new SynchronizedSortedMap (m); }

	/** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map, using an assigned object to synchronize.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @param sync an object that will be used to synchronize the access to the sorted sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */

	public static Long2CharSortedMap synchronize(final Long2CharSortedMap m, final Object sync) { return new SynchronizedSortedMap (m, sync); }




	/** An unmodifiable wrapper class for sorted maps. */

	public static class UnmodifiableSortedMap extends Long2CharMaps.UnmodifiableMap implements Long2CharSortedMap , java.io.Serializable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected final Long2CharSortedMap sortedMap;

	 protected UnmodifiableSortedMap(final Long2CharSortedMap m) {
	  super(m);
	  sortedMap = m;
	 }

	 @Override
	 public LongComparator comparator() { return sortedMap.comparator(); }

	 @Override
	 public ObjectSortedSet<Long2CharMap.Entry > long2CharEntrySet() { if (entries == null) entries = ObjectSortedSets.unmodifiable(sortedMap.long2CharEntrySet()); return (ObjectSortedSet<Long2CharMap.Entry >)entries; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated



	 @Override
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	 public ObjectSortedSet<Map.Entry<Long, Character>> entrySet() { return (ObjectSortedSet)long2CharEntrySet(); }

	 @Override
	 public LongSortedSet keySet() { if (keys == null) keys = LongSortedSets.unmodifiable(sortedMap.keySet()); return (LongSortedSet )keys; }

	 @Override
	 public Long2CharSortedMap subMap(final long from, final long to) { return new UnmodifiableSortedMap (sortedMap.subMap(from, to)); }

	 @Override
	 public Long2CharSortedMap headMap(final long to) { return new UnmodifiableSortedMap (sortedMap.headMap(to)); }

	 @Override
	 public Long2CharSortedMap tailMap(final long from) { return new UnmodifiableSortedMap (sortedMap.tailMap(from)); }

	 @Override
	 public long firstLongKey() { return sortedMap.firstLongKey(); }

	 @Override
	 public long lastLongKey() { return sortedMap.lastLongKey(); }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long firstKey() { return sortedMap.firstKey(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long lastKey() { return sortedMap.lastKey(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap subMap(final Long from, final Long to) { return new UnmodifiableSortedMap (sortedMap.subMap(from, to)); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap headMap(final Long to) { return new UnmodifiableSortedMap (sortedMap.headMap(to)); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long2CharSortedMap tailMap(final Long from) { return new UnmodifiableSortedMap (sortedMap.tailMap(from)); }



	}

	/** Returns an unmodifiable type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in an unmodifiable sorted map.
	 * @return an unmodifiable view of the specified sorted map.
	 * @see java.util.Collections#unmodifiableSortedMap(SortedMap)
	 */
	public static Long2CharSortedMap unmodifiable(final Long2CharSortedMap m) { return new UnmodifiableSortedMap (m); }
}

