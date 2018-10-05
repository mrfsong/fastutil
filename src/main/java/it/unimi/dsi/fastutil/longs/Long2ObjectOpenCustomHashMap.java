
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

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static it.unimi.dsi.fastutil.HashCommon.arraySize;
import static it.unimi.dsi.fastutil.HashCommon.maxFill;



/** A type-specific hash map with a fast, small-footprint implementation whose {@linkplain it.unimi.dsi.fastutil.Hash.Strategy hashing strategy}
	* is specified at creation time.
	*
	* <p>Instances of this class use a hash table to represent a map. The table is
	* filled up to a specified <em>load factor</em>, and then doubled in size to
	* accommodate new entries. If the table is emptied below <em>one fourth</em>
	* of the load factor, it is halved in size; however, the table is never reduced to a
	* size smaller than that at creation time: this approach makes it
	* possible to create maps with a large capacity in which insertions and
	* deletions do not cause immediately rehashing. Moreover, halving is
	* not performed when deleting entries from an iterator, as it would interfere
	* with the iteration process.
	*
	* <p>Note that {@link #clear()} does not modify the hash table size.
	* Rather, a family of {@linkplain #trim() trimming
	* methods} lets you control the size of the table; this is particularly useful
	* if you reuse instances of this class.
	*
	* @see Hash
	* @see HashCommon
	*/

public class Long2ObjectOpenCustomHashMap <V> extends AbstractLong2ObjectMap <V> implements java.io.Serializable, Cloneable, Hash {
	private static final long serialVersionUID = 0L;
	private static final boolean ASSERTS = false;

	/** The array of keys. */
	protected transient long[] key;

	/** The array of values. */
	protected transient V[] value;

	/** The mask for wrapping a position counter. */
	protected transient int mask;

	/** Whether this map contains the key zero. */
	protected transient boolean containsNullKey;


	/** The hash strategy of this custom map. */
	protected it.unimi.dsi.fastutil.longs.LongHash.Strategy strategy;
	/** The current table size. */
	protected transient int n;

	/** Threshold after which we rehash. It must be the table size times {@link #f}. */
	protected transient int maxFill;

	/** We never resize below this threshold, which is the construction-time {#n}. */
	protected final transient int minN;

	/** Number of entries in the set (including the key zero, if present). */
	protected int size;

	/** The acceptable load factor. */
	protected final float f;
	/** Cached set of entries. */
	protected transient FastEntrySet <V> entries;

	/** Cached set of keys. */
	protected transient LongSet keys;


	/** Cached collection of values. */
	protected transient ObjectCollection <V> values;



	/** Creates a new hash map.
	 *
	 * <p>The actual table size will be the least power of two greater than {@code expected}/{@code f}.
	 *
	 * @param expected the expected number of elements in the hash map.
	 * @param f the load factor.
	 * @param strategy the strategy.
	 */
	@SuppressWarnings("unchecked")
	public Long2ObjectOpenCustomHashMap(final int expected, final float f, final it.unimi.dsi.fastutil.longs.LongHash.Strategy strategy) {
	 this.strategy = strategy;
	 if (f <= 0 || f > 1) throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
	 if (expected < 0) throw new IllegalArgumentException("The expected number of elements must be nonnegative");

	 this.f = f;

	 minN = n = arraySize(expected, f);
	 mask = n - 1;
	 maxFill = maxFill(n, f);
	 key = new long[n + 1];
	 value = (V[]) new Object[n + 1];



	}



	/** Creates a new hash map with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor.
	 *
	 * @param expected the expected number of elements in the hash map.
	 * @param strategy the strategy.
	 */

	public Long2ObjectOpenCustomHashMap(final int expected, final it.unimi.dsi.fastutil.longs.LongHash.Strategy strategy) {
	 this(expected, DEFAULT_LOAD_FACTOR, strategy);
	}
	/** Creates a new hash map with initial expected {@link Hash#DEFAULT_INITIAL_SIZE} entries
	 * and {@link Hash#DEFAULT_LOAD_FACTOR} as load factor.
	 * @param strategy the strategy.
	 */

	public Long2ObjectOpenCustomHashMap(final it.unimi.dsi.fastutil.longs.LongHash.Strategy strategy) {
	 this(DEFAULT_INITIAL_SIZE, DEFAULT_LOAD_FACTOR, strategy);
	}
	/** Creates a new hash map copying a given one.
	 *
	 * @param m a {@link Map} to be copied into the new hash map.
	 * @param f the load factor.
	 * @param strategy the strategy.
	 */

	public Long2ObjectOpenCustomHashMap(final Map<? extends Long, ? extends V> m, final float f, final it.unimi.dsi.fastutil.longs.LongHash.Strategy strategy) {
	 this(m.size(), f, strategy);
	 putAll(m);
	}
	/** Creates a new hash map with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor copying a given one.
	 *
	 * @param m a {@link Map} to be copied into the new hash map.
	 * @param strategy the strategy.
	 */

	public Long2ObjectOpenCustomHashMap(final Map<? extends Long, ? extends V> m, final it.unimi.dsi.fastutil.longs.LongHash.Strategy strategy) {
	 this(m, DEFAULT_LOAD_FACTOR, strategy);
	}
	/** Creates a new hash map copying a given type-specific one.
	 *
	 * @param m a type-specific map to be copied into the new hash map.
	 * @param f the load factor.
	 * @param strategy the strategy.
	 */

	public Long2ObjectOpenCustomHashMap(final Long2ObjectMap <V> m, final float f, final it.unimi.dsi.fastutil.longs.LongHash.Strategy strategy) {
	 this(m.size(), f, strategy);
	 putAll(m);
	}
	/** Creates a new hash map with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor copying a given type-specific one.
	 *
	 * @param m a type-specific map to be copied into the new hash map.
	 * @param strategy the strategy.
	 */

	public Long2ObjectOpenCustomHashMap(final Long2ObjectMap <V> m, final it.unimi.dsi.fastutil.longs.LongHash.Strategy strategy) {
	 this(m, DEFAULT_LOAD_FACTOR, strategy);
	}
	/** Creates a new hash map using the elements of two parallel arrays.
	 *
	 * @param k the array of keys of the new hash map.
	 * @param v the array of corresponding values in the new hash map.
	 * @param f the load factor.
	 * @param strategy the strategy.
	 * @throws IllegalArgumentException if {@code k} and {@code v} have different lengths.
	 */

	public Long2ObjectOpenCustomHashMap(final long[] k, final V[] v, final float f, final it.unimi.dsi.fastutil.longs.LongHash.Strategy strategy) {
	 this(k.length, f, strategy);
	 if (k.length != v.length) throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
	 for(int i = 0; i < k.length; i++) this.put(k[i], v[i]);
	}
	/** Creates a new hash map with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor using the elements of two parallel arrays.
	 *
	 * @param k the array of keys of the new hash map.
	 * @param v the array of corresponding values in the new hash map.
	 * @param strategy the strategy.
	 * @throws IllegalArgumentException if {@code k} and {@code v} have different lengths.
	 */

	public Long2ObjectOpenCustomHashMap(final long[] k, final V[] v, final it.unimi.dsi.fastutil.longs.LongHash.Strategy strategy) {
	 this(k, v, DEFAULT_LOAD_FACTOR, strategy);
	}
	/** Returns the hashing strategy.
	 *
	 * @return the hashing strategy of this custom hash map.
	 */

	public it.unimi.dsi.fastutil.longs.LongHash.Strategy strategy() {
	 return strategy;
	}


	private int realSize() {
	 return containsNullKey ? size - 1 : size;
	}

	private void ensureCapacity(final int capacity) {
	 final int needed = arraySize(capacity, f);
	 if (needed > n) rehash(needed);
	}

	private void tryCapacity(final long capacity) {
	 final int needed = (int)Math.min(1 << 30, Math.max(2, HashCommon.nextPowerOfTwo((long)Math.ceil(capacity / f))));
	 if (needed > n) rehash(needed);
	}

	private V removeEntry(final int pos) {
	 final V oldValue = value[pos];

	 value[pos] = null;

	 size--;




	 shiftKeys(pos);
	 if (n > minN && size < maxFill / 4 && n > DEFAULT_INITIAL_SIZE) rehash(n / 2);
	 return oldValue;
	}

	private V removeNullEntry() {
	 containsNullKey = false;



	 final V oldValue = value[n];

	 value[n] = null;

	 size--;



	 if (n > minN && size < maxFill / 4 && n > DEFAULT_INITIAL_SIZE) rehash(n / 2);
	 return oldValue;
	}


	@Override
	public void putAll(Map<? extends Long,? extends V> m) {
	 if (f <= .5) ensureCapacity(m.size()); // The resulting map will be sized for m.size() elements
	 else tryCapacity(size() + m.size()); // The resulting map will be tentatively sized for size() + m.size() elements
	 super.putAll(m);
	}


	private int find(final long k) {
	 if (( strategy.equals( (k), (0) ) )) return containsNullKey ? n : -(n + 1);

	 long curr;
	 final long[] key = this.key;
	 int pos;

	 // The starting point.
	 if (( (curr = key[pos = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(k) ) ) & mask]) == (0) )) return -(pos + 1);
	 if (( strategy.equals( (k), (curr) ) )) return pos;
	 // There's always an unused entry.
	 while(true) {
	  if (( (curr = key[pos = (pos + 1) & mask]) == (0) )) return -(pos + 1);
	  if (( strategy.equals( (k), (curr) ) )) return pos;
	 }
	}


	private void insert(final int pos, final long k, final V v) {
	 if (pos == n) containsNullKey = true;
	 key[pos] = k;
	 value[pos] = v;
	 if (size++ >= maxFill) rehash(arraySize(size + 1, f));
	 if (ASSERTS) checkTable();
	}


	@Override
	public V put(final long k, final V v) {
	 final int pos = find(k);
	 if (pos < 0) {
	  insert(-pos - 1, k, v);
	  return defRetValue;
	 }
	 final V oldValue = value[pos];
	 value[pos] = v;
	 return oldValue;
	}
	/** Shifts left entries with the specified hash code, starting at the specified position,
	 * and empties the resulting free entry.
	 *
	 * @param pos a starting position.
	 */
	protected final void shiftKeys(int pos) {
	 // Shift entries with the same hash.
	 int last, slot;
	 long curr;
	 final long[] key = this.key;

	 for(;;) {
	  pos = ((last = pos) + 1) & mask;

	  for(;;) {
	   if (( (curr = key[pos]) == (0) )) {
	    key[last] = (0);

	    value[last] = null;

	    return;
	   }
	   slot = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(curr) ) ) & mask;
	   if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
	   pos = (pos + 1) & mask;
	  }

	  key[last] = curr;
	  value[last] = value[pos];



	 }
	}

	@Override

	public V remove(final long k) {
	 if (( strategy.equals( (k), (0) ) )) {
	  if (containsNullKey) return removeNullEntry();
	  return defRetValue;
	 }

	 long curr;
	 final long[] key = this.key;
	 int pos;

	 // The starting point.
	 if (( (curr = key[pos = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(k) ) ) & mask]) == (0) )) return defRetValue;
	 if (( strategy.equals( (k), (curr) ) )) return removeEntry(pos);
	 while(true) {
	  if (( (curr = key[pos = (pos + 1) & mask]) == (0) )) return defRetValue;
	  if (( strategy.equals( (k), (curr) ) )) return removeEntry(pos);
	 }
	}
	@Override

	public V get(final long k) {
	 if (( strategy.equals( (k), (0) ) )) return containsNullKey ? value[n] : defRetValue;

	 long curr;
	 final long[] key = this.key;
	 int pos;

	 // The starting point.
	 if (( (curr = key[pos = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(k) ) ) & mask]) == (0) )) return defRetValue;
	 if (( strategy.equals( (k), (curr) ) )) return value[pos];
	 // There's always an unused entry.
	 while(true) {
	  if (( (curr = key[pos = (pos + 1) & mask]) == (0) )) return defRetValue;
	  if (( strategy.equals( (k), (curr) ) )) return value[pos];
	 }
	}

	@Override

	public boolean containsKey(final long k) {
	 if (( strategy.equals( (k), (0) ) )) return containsNullKey;

	 long curr;
	 final long[] key = this.key;
	 int pos;

	 // The starting point.
	 if (( (curr = key[pos = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(k) ) ) & mask]) == (0) )) return false;
	 if (( strategy.equals( (k), (curr) ) )) return true;
	 // There's always an unused entry.
	 while(true) {
	  if (( (curr = key[pos = (pos + 1) & mask]) == (0) )) return false;
	  if (( strategy.equals( (k), (curr) ) )) return true;
	 }
	}


	@Override
	public boolean containsValue(final Object v) {
	 final V value[] = this.value;
	 final long key[] = this.key;
	 if (containsNullKey && java.util.Objects.equals(value[n], v)) return true;
	 for(int i = n; i-- != 0;) if (! ( (key[i]) == (0) ) && java.util.Objects.equals(value[i], v)) return true;
	 return false;
	}




	/** {@inheritDoc} */
	@Override

	public V getOrDefault(final long k, final V defaultValue) {
	 if (( strategy.equals( (k), (0) ) )) return containsNullKey ? value[n] : defaultValue;

	 long curr;
	 final long[] key = this.key;
	 int pos;

	 // The starting point.
	 if (( (curr = key[pos = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(k) ) ) & mask]) == (0) )) return defaultValue;
	 if (( strategy.equals( (k), (curr) ) )) return value[pos];
	 // There's always an unused entry.
	 while(true) {
	  if (( (curr = key[pos = (pos + 1) & mask]) == (0) )) return defaultValue;
	  if (( strategy.equals( (k), (curr) ) )) return value[pos];
	 }
	}

	/** {@inheritDoc} */
	@Override
	public V putIfAbsent(final long k, final V v) {
	 final int pos = find(k);
	 if (pos >= 0) return value[pos];
	 insert(-pos - 1, k, v);
	 return defRetValue;
	}

	/** {@inheritDoc} */
	@Override

	public boolean remove(final long k, final Object v) {
	 if (( strategy.equals( (k), (0) ) )) {
	  if (containsNullKey && java.util.Objects.equals(v, value[n])) {
	   removeNullEntry();
	   return true;
	  }
	  return false;
	 }

	 long curr;
	 final long[] key = this.key;
	 int pos;

	 // The starting point.
	 if (( (curr = key[pos = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(k) ) ) & mask]) == (0) )) return false;
	 if (( strategy.equals( (k), (curr) ) ) && java.util.Objects.equals(v, value[pos])) {
	  removeEntry(pos);
	  return true;
	 }
	 while(true) {
	  if (( (curr = key[pos = (pos + 1) & mask]) == (0) )) return false;
	  if (( strategy.equals( (k), (curr) ) ) && java.util.Objects.equals(v, value[pos])) {
	   removeEntry(pos);
	   return true;
	  }
	 }
	}

	/** {@inheritDoc} */
	@Override
	public boolean replace(final long k, final V oldValue, final V v) {
	 final int pos = find(k);
	 if (pos < 0 || ! java.util.Objects.equals(oldValue, value[pos])) return false;
	 value[pos] = v;
	 return true;
	}

	/** {@inheritDoc} */
	@Override
	public V replace(final long k, final V v) {
	 final int pos = find(k);
	 if (pos < 0) return defRetValue;
	 final V oldValue = value[pos];
	 value[pos] = v;
	 return oldValue;
	}



	/** {@inheritDoc} */
	@Override
	public V computeIfAbsent(final long k, final java.util.function.LongFunction <? extends V> mappingFunction) {
	 java.util.Objects.requireNonNull(mappingFunction);
	 final int pos = find(k);
	 if (pos >= 0) return value[pos];
	 final V newValue = mappingFunction.apply(k);
	 insert(-pos -1, k, newValue);
	 return newValue;
	}
	/** {@inheritDoc} */
	@Override
	public V computeIfPresent(final long k, final java.util.function.BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
	 java.util.Objects.requireNonNull(remappingFunction);
	 final int pos = find(k);
	 if (pos < 0) return defRetValue;
	 final V newValue = remappingFunction.apply(Long.valueOf(k), (value[pos]));
	 if (newValue == null) {
	  if (( strategy.equals( (k), (0) ) )) removeNullEntry();
	  else removeEntry(pos);
	  return defRetValue;
	 }
	 return value[pos] = (newValue);
	}

	/** {@inheritDoc} */
	@Override
	public V compute(final long k, final java.util.function.BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
	 java.util.Objects.requireNonNull(remappingFunction);
	 final int pos = find(k);
	 final V newValue = remappingFunction.apply(Long.valueOf(k), pos >= 0 ? (value[pos]) : null);
	 if (newValue == null) {
	  if (pos >= 0) {
	   if (( strategy.equals( (k), (0) ) )) removeNullEntry();
	   else removeEntry(pos);
	  }
	  return defRetValue;
	 }

	 V newVal = (newValue);
	 if (pos < 0) {
	  insert(-pos - 1, k, newVal);
	  return newVal;
	 }

	 return value[pos] = newVal;
	}

	/** {@inheritDoc} */
	@Override
	public V merge(final long k, final V v, final java.util.function.BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
	 java.util.Objects.requireNonNull(remappingFunction);

	 final int pos = find(k);



	 if (pos < 0 || value[pos] == null) {
	  if (v == null) return defRetValue;

	  insert(-pos - 1, k, v);
	  return v;
	 }

	 final V newValue = remappingFunction.apply((value[pos]), (v));
	 if (newValue == null) {
	  if (( strategy.equals( (k), (0) ) )) removeNullEntry();
	  else removeEntry(pos);
	  return defRetValue;
	 }

	 return value[pos] = (newValue);
	}




	/* Removes all elements from this map.
	 *
	 * <p>To increase object reuse, this method does not change the table size.
	 * If you want to reduce the table size, you must use {@link #trim()}.
	 *
	 */
	@Override
	public void clear() {
	 if (size == 0) return;
	 size = 0;
	 containsNullKey = false;

	 Arrays.fill(key, (0));

	 Arrays.fill(value, null);





	}

	@Override
	public int size() {
	 return size;
	}

	@Override
	public boolean isEmpty() {
	 return size == 0;
	}


	/** The entry class for a hash map does not record key and value, but
	 * rather the position in the hash table of the corresponding entry. This
	 * is necessary so that calls to {@link java.util.Map.Entry#setValue(Object)} are reflected in
	 * the map */

	final class MapEntry implements Long2ObjectMap.Entry <V>, Map.Entry<Long, V> {
	 // The table index this entry refers to, or -1 if this entry has been deleted.
	 int index;

	 MapEntry(final int index) {
	  this.index = index;
	 }

	 MapEntry() {}

	 @Override
	 public long getLongKey() {
	     return key[index];
	 }

	 @Override
	 public V getValue() {
	  return value[index];
	 }

	 @Override
	 public V setValue(final V v) {
	  final V oldValue = value[index];
	  value[index] = v;
	  return oldValue;
	 }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long getKey() {
	  return Long.valueOf(key[index]);
	 }
	 @SuppressWarnings("unchecked")
	 @Override
	 public boolean equals(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;
	  Map.Entry<Long, V> e = (Map.Entry<Long, V>)o;

	  return ( strategy.equals( (key[index]), ((e.getKey()).longValue()) ) ) && java.util.Objects.equals(value[index], (e.getValue()));
	 }

	 @Override
	 public int hashCode() {
	  return ( strategy.hashCode(key[index]) ) ^ ( (value[index]) == null ? 0 : (value[index]).hashCode() );
	 }

	 @Override
	 public String toString() {
	  return key[index] + "=>" + value[index];
	 }
	}
	/** An iterator over a hash map. */

	private class MapIterator {
	 /** The index of the last entry returned, if positive or zero; initially, {@link #n}. If negative, the last
			entry returned was that of the key of index {@code - pos - 1} from the {@link #wrapped} list. */
	 int pos = n;
	 /** The index of the last entry that has been returned (more precisely, the value of {@link #pos} if {@link #pos} is positive,
			or {@link Integer#MIN_VALUE} if {@link #pos} is negative). It is -1 if either
			we did not return an entry yet, or the last returned entry has been removed. */
	 int last = -1;
	 /** A downward counter measuring how many entries must still be returned. */
	 int c = size;
	 /** A boolean telling us whether we should return the entry with the null key. */
	 boolean mustReturnNullKey = Long2ObjectOpenCustomHashMap.this.containsNullKey;
	 /** A lazily allocated list containing keys of entries that have wrapped around the table because of removals. */
	 LongArrayList wrapped;

	 public boolean hasNext() {
	  return c != 0;
	 }

	 public int nextEntry() {
	  if (! hasNext()) throw new NoSuchElementException();

	  c--;
	  if (mustReturnNullKey) {
	   mustReturnNullKey = false;
	   return last = n;
	  }

	  final long key[] = Long2ObjectOpenCustomHashMap.this.key;

	  for(;;) {
	   if (--pos < 0) {
	    // We are just enumerating elements from the wrapped list.
	    last = Integer.MIN_VALUE;
	    final long k = wrapped.getLong(- pos - 1);
	    int p = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(k) ) ) & mask;
	    while (! ( strategy.equals( (k), (key[p]) ) )) p = (p + 1) & mask;
	    return p;
	   }
	   if (! ( (key[pos]) == (0) )) return last = pos;
	  }
	 }

	 /** Shifts left entries with the specified hash code, starting at the specified position,
		 * and empties the resulting free entry.
		 *
		 * @param pos a starting position.
		 */
	 private void shiftKeys(int pos) {
	  // Shift entries with the same hash.
	  int last, slot;
	  long curr;
	  final long[] key = Long2ObjectOpenCustomHashMap.this.key;

	  for(;;) {
	   pos = ((last = pos) + 1) & mask;

	   for(;;) {
	    if (( (curr = key[pos]) == (0) )) {
	     key[last] = (0);

	     value[last] = null;

	     return;
	    }
	    slot = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(curr) ) ) & mask;
	    if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
	    pos = (pos + 1) & mask;
	   }

	   if (pos < last) { // Wrapped entry.
	    if (wrapped == null) wrapped = new LongArrayList (2);
	    wrapped.add(key[pos]);
	   }

	   key[last] = curr;
	   value[last] = value[pos];
	  }
	 }

	 public void remove() {
	  if (last == -1) throw new IllegalStateException();
	  if (last == n) {
	   containsNullKey = false;




	   value[n] = null;

	  }
	  else if (pos >= 0) shiftKeys(last);
	  else {
	   // We're removing wrapped entries.



	   Long2ObjectOpenCustomHashMap.this.remove(wrapped.getLong(- pos - 1));

	   last = -1; // Note that we must not decrement size
	   return;
	  }

	  size--;
	  last = -1; // You can no longer remove this entry.
	  if (ASSERTS) checkTable();
	 }

	 public int skip(final int n) {
	  int i = n;
	  while(i-- != 0 && hasNext()) nextEntry();
	  return n - i - 1;
	 }
	}


	private class EntryIterator extends MapIterator implements ObjectIterator<Long2ObjectMap.Entry <V> > {
	 private MapEntry entry;

	 @Override
	 public MapEntry next() {
	  return entry = new MapEntry(nextEntry());
	 }

	 @Override
	 public void remove() {
	  super.remove();
	  entry.index = -1; // You cannot use a deleted entry.
	 }
	}

	private class FastEntryIterator extends MapIterator implements ObjectIterator<Long2ObjectMap.Entry <V> > {
	 private final MapEntry entry = new MapEntry();

	 @Override
	 public MapEntry next() {
	  entry.index = nextEntry();
	  return entry;
	 }
	}
	private final class MapEntrySet extends AbstractObjectSet<Long2ObjectMap.Entry <V> > implements FastEntrySet <V> {

	 @Override
	 public ObjectIterator<Long2ObjectMap.Entry <V> > iterator() { return new EntryIterator(); }

	 @Override
	 public ObjectIterator<Long2ObjectMap.Entry <V> > fastIterator() { return new FastEntryIterator(); }


	 @Override
	 @SuppressWarnings("unchecked")
	 public boolean contains(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;
	  final Map.Entry<?,?> e = (Map.Entry<?,?>)o;

	  if (e.getKey() == null || ! (e.getKey() instanceof Long)) return false;




	  final long k = ((Long)(e.getKey())).longValue();
	  final V v = ((V) e.getValue());

	  if (( strategy.equals( (k), (0) ) )) return Long2ObjectOpenCustomHashMap.this.containsNullKey && java.util.Objects.equals(value[n], v);

	  long curr;
	  final long[] key = Long2ObjectOpenCustomHashMap.this.key;
	  int pos;

	  // The starting point.
	  if (( (curr = key[pos = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(k) ) ) & mask]) == (0) )) return false;
	  if (( strategy.equals( (k), (curr) ) )) return java.util.Objects.equals(value[pos], v);
	  // There's always an unused entry.
	  while(true) {
	   if (( (curr = key[pos = (pos + 1) & mask]) == (0) )) return false;
	   if (( strategy.equals( (k), (curr) ) )) return java.util.Objects.equals(value[pos], v);
	  }
	 }

	 @Override
	 @SuppressWarnings("unchecked")
	 public boolean remove(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;
	  final Map.Entry<?,?> e = (Map.Entry<?,?>)o;

	  if (e.getKey() == null || ! (e.getKey() instanceof Long)) return false;




	  final long k = ((Long)(e.getKey())).longValue();
	  final V v = ((V) e.getValue());


	  if (( strategy.equals( (k), (0) ) )) {
	   if (containsNullKey && java.util.Objects.equals(value[n], v)) {
	    removeNullEntry();
	    return true;
	   }
	   return false;
	  }

	  long curr;
	  final long[] key = Long2ObjectOpenCustomHashMap.this.key;
	  int pos;

	  // The starting point.
	  if (( (curr = key[pos = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(k) ) ) & mask]) == (0) )) return false;
	  if (( strategy.equals( (curr), (k) ) )) {
	   if (java.util.Objects.equals(value[pos], v)) {
	    removeEntry(pos);
	    return true;
	   }
	   return false;
	  }

	  while(true) {
	   if (( (curr = key[pos = (pos + 1) & mask]) == (0) )) return false;
	   if (( strategy.equals( (curr), (k) ) )) {
	    if (java.util.Objects.equals(value[pos], v)) {
	     removeEntry(pos);
	     return true;
	    }
	   }
	  }
	 }

	 @Override
	 public int size() {
	  return size;
	 }

	 @Override
	 public void clear() {
	  Long2ObjectOpenCustomHashMap.this.clear();
	 }
	 /** {@inheritDoc} */
	 @Override
	 public void forEach(final Consumer<? super Long2ObjectMap.Entry <V> > consumer) {
	  if (containsNullKey) consumer.accept(new AbstractLong2ObjectMap.BasicEntry <V>(key[n], value[n]));
	  for(int pos = n; pos-- != 0;)
	   if (! ( (key[pos]) == (0) )) consumer.accept(new AbstractLong2ObjectMap.BasicEntry <V>(key[pos], value[pos]));
	 }

	 /** {@inheritDoc} */
	 @Override
	 public void fastForEach(final Consumer<? super Long2ObjectMap.Entry <V> > consumer) {
	  final AbstractLong2ObjectMap.BasicEntry <V> entry = new AbstractLong2ObjectMap.BasicEntry <>();
	  if (containsNullKey) {
	   entry.key = key[n];
	   entry.value = value[n];
	   consumer.accept(entry);
	  }
	  for(int pos = n; pos-- != 0;)
	   if (! ( (key[pos]) == (0) )) {
	    entry.key = key[pos];
	    entry.value = value[pos];
	    consumer.accept(entry);
	   }
	 }


	}







	@Override
	public FastEntrySet <V> long2ObjectEntrySet() {
	 if (entries == null) entries = new MapEntrySet();

	 return entries;
	}

	/** An iterator on keys.
	 *
	 * <p>We simply override the {@link java.util.ListIterator#next()}/{@link java.util.ListIterator#previous()} methods
	 * (and possibly their type-specific counterparts) so that they return keys
	 * instead of entries.
	 */
	private final class KeyIterator extends MapIterator implements LongIterator {

	 public KeyIterator() { super(); }

	 @Override
	 public long nextLong() { return key[nextEntry()]; }
	}
	private final class KeySet extends AbstractLongSet {

	 @Override
	 public LongIterator iterator() { return new KeyIterator(); }


	 /** {@inheritDoc} */
	 @Override

	 public void forEach(final java.util.function.LongConsumer consumer) {



	  if (containsNullKey) consumer.accept(key[n]);
	  for(int pos = n; pos-- != 0;) {
	   final long k = key[pos];
	   if (! ( (k) == (0) )) consumer.accept(k);
	  }
	 }

	 @Override
	 public int size() { return size; }

	 @Override
	 public boolean contains(long k) { return containsKey(k); }

	 @Override
	 public boolean remove(long k) {
	  final int oldSize = size;
	  Long2ObjectOpenCustomHashMap.this.remove(k);
	  return size != oldSize;
	 }

	 @Override
	 public void clear() { Long2ObjectOpenCustomHashMap.this.clear();}
	}






	@Override
	public LongSet keySet() {

	 if (keys == null) keys = new KeySet();
	 return keys;
	}


	/** An iterator on values.
	 *
	 * <p>We simply override the {@link java.util.ListIterator#next()}/{@link java.util.ListIterator#previous()} methods
	 * (and possibly their type-specific counterparts) so that they return values
	 * instead of entries.
	 */







	private final class ValueIterator extends MapIterator implements ObjectIterator <V> {

	 public ValueIterator() { super(); }

	 @Override
	 public V next() { return value[nextEntry()]; }
	}

	@Override
	public ObjectCollection <V> values() {
	 if (values == null) values = new AbstractObjectCollection <V>() {
	   @Override
	   public ObjectIterator <V> iterator() { return new ValueIterator(); }
	   @Override
	   public int size() { return size; }
	   @Override
	   public boolean contains(Object v) { return containsValue(v); }
	   @Override
	   public void clear() { Long2ObjectOpenCustomHashMap.this.clear(); }

	   /** {@inheritDoc} */
	   @Override



	   public void forEach(final Consumer <? super V> consumer) {

	    if (containsNullKey) consumer.accept(value[n]);
	    for(int pos = n; pos-- != 0;)
	     if (! ( (key[pos]) == (0) )) consumer.accept(value[pos]);
	   }

	  };

	 return values;
	}


	/** Rehashes the map, making the table as small as possible.
	 *
	 * <p>This method rehashes the table to the smallest size satisfying the
	 * load factor. It can be used when the set will not be changed anymore, so
	 * to optimize access speed and size.
	 *
	 * <p>If the table size is already the minimum possible, this method
	 * does nothing.
	 *
	 * @return true if there was enough memory to trim the map.
	 * @see #trim(int)
	 */

	public boolean trim() {
	 final int l = arraySize(size, f);
	 if (l >= n || size > maxFill(l, f)) return true;
	 try {
	  rehash(l);
	 }
	 catch(OutOfMemoryError cantDoIt) { return false; }
	 return true;
	}


	/** Rehashes this map if the table is too large.
	 *
	 * <p>Let <var>N</var> be the smallest table size that can hold
	 * <code>max(n,{@link #size()})</code> entries, still satisfying the load factor. If the current
	 * table size is smaller than or equal to <var>N</var>, this method does
	 * nothing. Otherwise, it rehashes this map in a table of size
	 * <var>N</var>.
	 *
	 * <p>This method is useful when reusing maps.  {@linkplain #clear() Clearing a
	 * map} leaves the table size untouched. If you are reusing a map
	 * many times, you can call this method with a typical
	 * size to avoid keeping around a very large table just
	 * because of a few large transient maps.
	 *
	 * @param n the threshold for the trimming.
	 * @return true if there was enough memory to trim the map.
	 * @see #trim()
	 */

	public boolean trim(final int n) {
	 final int l = HashCommon.nextPowerOfTwo((int)Math.ceil(n / f));
	 if (l >= n || size > maxFill(l, f)) return true;
	 try {
	  rehash(l);
	 }
	 catch(OutOfMemoryError cantDoIt) { return false; }
	 return true;
	}

	/** Rehashes the map.
	 *
	 * <p>This method implements the basic rehashing strategy, and may be
	 * overridden by subclasses implementing different rehashing strategies (e.g.,
	 * disk-based rehashing). However, you should not override this method
	 * unless you understand the internal workings of this class.
	 *
	 * @param newN the new size
	 */

	@SuppressWarnings("unchecked")
	protected void rehash(final int newN) {
	 final long key[] = this.key;
	 final V value[] = this.value;

	 final int mask = newN - 1; // Note that this is used by the hashing macro
	 final long newKey[] = new long[newN + 1];
	 final V newValue[] = (V[]) new Object[newN + 1];
	 int i = n, pos;

	 for(int j = realSize(); j-- != 0;) {
	  while(( (key[--i]) == (0) ));

	  if (! ( (newKey[pos = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(key[i]) ) ) & mask]) == (0) ))
	   while (! ( (newKey[pos = (pos + 1) & mask]) == (0) ));

	  newKey[pos] = key[i];
	  newValue[pos] = value[i];
	 }

	 newValue[newN] = value[n];



	 n = newN;
	 this.mask = mask;
	 maxFill = maxFill(n, f);
	 this.key = newKey;
	 this.value = newValue;
	}


	/** Returns a deep copy of this map.
	 *
	 * <p>This method performs a deep copy of this hash map; the data stored in the
	 * map, however, is not cloned. Note that this makes a difference only for object keys.
	 *
	 *  @return a deep copy of this map.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Long2ObjectOpenCustomHashMap <V> clone() {
	 Long2ObjectOpenCustomHashMap <V> c;
	 try {
	  c = (Long2ObjectOpenCustomHashMap <V>)super.clone();
	 }
	 catch(CloneNotSupportedException cantHappen) {
	  throw new InternalError();
	 }

	 c.keys = null;
	 c.values = null;
	 c.entries = null;
	 c.containsNullKey = containsNullKey;

	 c.key = key.clone();
	 c.value = value.clone();




	 c.strategy = strategy;

	 return c;
	}


	/** Returns a hash code for this map.
	 *
	 * This method overrides the generic method provided by the superclass.
	 * Since {@code equals()} is not overriden, it is important
	 * that the value returned by this method is the same value as
	 * the one returned by the overriden method.
	 *
	 * @return a hash code for this map.
	 */

	@Override
	public int hashCode() {
	 int h = 0;
	 for(int j = realSize(), i = 0, t = 0; j-- != 0;) {
	  while(( (key[i]) == (0) )) i++;



	   t = ( strategy.hashCode(key[i]) );

	  if (this != value[i])

	   t ^= ( (value[i]) == null ? 0 : (value[i]).hashCode() );
	  h += t;
	  i++;
	 }
	 // Zero / null keys have hash zero.
	 if (containsNullKey) h += ( (value[n]) == null ? 0 : (value[n]).hashCode() );
	 return h;
	}



	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
	 final long key[] = this.key;
	 final V value[] = this.value;
	 final MapIterator i = new MapIterator();

	 s.defaultWriteObject();

	 for(int j = size, e; j-- != 0;) {
	  e = i.nextEntry();
	  s.writeLong(key[e]);
	  s.writeObject(value[e]);
	 }
	}



	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
	 s.defaultReadObject();

	 n = arraySize(size, f);
	 maxFill = maxFill(n, f);
	 mask = n - 1;

	 final long key[] = this.key = new long[n + 1];
	 final V value[] = this.value = (V[]) new Object[n + 1];






	 long k;
	 V v;

	 for(int i = size, pos; i-- != 0;) {

	  k = s.readLong();
	  v = (V) s.readObject();

	  if (( strategy.equals( (k), (0) ) )) {
	   pos = n;
	   containsNullKey = true;
	  }
	  else {
	   pos = ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(k) ) ) & mask;
	   while (! ( (key[pos]) == (0) )) pos = (pos + 1) & mask;
	  }

	  key[pos] = k;
	  value[pos] = v;
	 }
	 if (ASSERTS) checkTable();
	}
	private void checkTable() {}
}
