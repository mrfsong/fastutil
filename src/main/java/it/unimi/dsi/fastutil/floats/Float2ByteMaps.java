
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


package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteCollections;
import it.unimi.dsi.fastutil.bytes.ByteSets;
import it.unimi.dsi.fastutil.floats.Float2ByteMap.FastEntrySet;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

import java.util.Map;
import java.util.function.Consumer;

/** A class providing static methods and objects that do useful things with type-specific maps.
	*
	* @see java.util.Collections
	*/

public final class Float2ByteMaps {

	private Float2ByteMaps() {}

	/** Returns an iterator that will be {@linkplain FastEntrySet fast}, if possible, on the {@linkplain Map#entrySet() entry set} of the provided {@code map}.
	 * @param map a map from which we will try to extract a (fast) iterator on the entry set.
	 * @return an iterator on the entry set of the given map that will be fast, if possible.
	 * @since 8.0.0
	 */

	public static ObjectIterator<Float2ByteMap.Entry > fastIterator(Float2ByteMap map) {
	 final ObjectSet<Float2ByteMap.Entry > entries = map.float2ByteEntrySet();
	 return entries instanceof Float2ByteMap.FastEntrySet ? ((Float2ByteMap.FastEntrySet ) entries).fastIterator() : entries.iterator();
	}

	/** Iterates {@linkplain FastEntrySet#fastForEach(Consumer) quickly}, if possible, on the {@linkplain Map#entrySet() entry set} of the provided {@code map}.
	 * @param map a map on which we will try to iterate {@linkplain FastEntrySet#fastForEach(Consumer) quickly}.
	 * @param consumer the consumer that will be passed to  {@link FastEntrySet#fastForEach(Consumer)}, if possible, or to {@link Iterable#forEach(Consumer)}.
	 * @since 8.1.0
	 */

	public static void fastForEach(Float2ByteMap map, final Consumer<? super Float2ByteMap.Entry > consumer) {
	 final ObjectSet<Float2ByteMap.Entry > entries = map.float2ByteEntrySet();
	 if (entries instanceof Float2ByteMap.FastEntrySet) ((Float2ByteMap.FastEntrySet ) entries).fastForEach(consumer);
	 else entries.forEach(consumer);
	}

	/** Returns an iterable yielding an iterator that will be {@linkplain FastEntrySet fast}, if possible, on the {@linkplain Map#entrySet() entry set} of the provided {@code map}.
	 * @param map a map from which we will try to extract an iterable yielding a (fast) iterator on the entry set.
	 * @return an iterable  yielding an iterator on the entry set of the given map that will be
	 * fast, if possible.
	 * @since 8.0.0
	 */

	public static ObjectIterable<Float2ByteMap.Entry > fastIterable(Float2ByteMap map) {
	 final ObjectSet<Float2ByteMap.Entry > entries = map.float2ByteEntrySet();
	 return entries instanceof Float2ByteMap.FastEntrySet ? new ObjectIterable<Float2ByteMap.Entry >() {
	  public ObjectIterator<Float2ByteMap.Entry > iterator() { return ((Float2ByteMap.FastEntrySet )entries).fastIterator(); }
	  public void forEach(final Consumer<? super Float2ByteMap.Entry > consumer) { ((Float2ByteMap.FastEntrySet )entries).fastForEach(consumer); }
	 } : entries;
	}

	/** An immutable class representing an empty type-specific map.
	 *
	 * <p>This class may be useful to implement your own in case you subclass
	 * a type-specific map.
	 */

	public static class EmptyMap extends Float2ByteFunctions.EmptyFunction implements Float2ByteMap , java.io.Serializable, Cloneable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected EmptyMap() {}

	 @Override
	 public boolean containsValue(final byte v) { return false; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public boolean containsValue(final Object ov) { return false; }


	 @Override
	 public void putAll(final Map<? extends Float, ? extends Byte> m) { throw new UnsupportedOperationException(); }

	 @SuppressWarnings("unchecked")
	 @Override
	 public ObjectSet<Float2ByteMap.Entry > float2ByteEntrySet() { return ObjectSets.EMPTY_SET; }


	 @Override
	 public FloatSet keySet() { return FloatSets.EMPTY_SET; }


	 @Override
	 public ByteCollection values() { return ByteSets.EMPTY_SET; }

	 @Override
	 public Object clone() { return EMPTY_MAP; }

	 @Override
	 public boolean isEmpty() { return true; }

	 @Override
	 public int hashCode() { return 0; }

	 @Override
	 public boolean equals(final Object o) {
	  if (! (o instanceof Map)) return false;
	  return ((Map<?,?>)o).isEmpty();
	 }

	 @Override
	 public String toString() { return "{}"; }
	}


	/** An empty type-specific map (immutable). It is serializable and cloneable.
	 */

	public static final EmptyMap EMPTY_MAP = new EmptyMap();
	/** An immutable class representing a type-specific singleton map.
	 *
	 * <p>This class may be useful to implement your own in case you subclass
	 * a type-specific map.
	 */

	public static class Singleton extends Float2ByteFunctions.Singleton implements Float2ByteMap , java.io.Serializable, Cloneable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected transient ObjectSet<Float2ByteMap.Entry > entries;
	 protected transient FloatSet keys;
	 protected transient ByteCollection values;

	 protected Singleton(final float key, final byte value) {
	  super(key, value);
	 }

	 @Override
	 public boolean containsValue(final byte v) { return ( (value) == (v) ); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public boolean containsValue(final Object ov) { return ( (((Byte)(ov)).byteValue()) == (value) ); }


	 @Override
	 public void putAll(final Map<? extends Float, ? extends Byte> m) { throw new UnsupportedOperationException(); }

	 @Override
	 public ObjectSet<Float2ByteMap.Entry > float2ByteEntrySet() { if (entries == null) entries = ObjectSets.singleton(new AbstractFloat2ByteMap.BasicEntry (key, value)); return entries; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated



	 @Override
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	 public ObjectSet<Map.Entry<Float, Byte>> entrySet() { return (ObjectSet)float2ByteEntrySet(); }

	 @Override
	 public FloatSet keySet() { if (keys == null) keys = FloatSets.singleton(key); return keys; }

	 @Override
	 public ByteCollection values() { if (values == null) values = ByteSets.singleton(value); return values; }

	 @Override
	 public boolean isEmpty() { return false; }

	 @Override
	 public int hashCode() { return it.unimi.dsi.fastutil.HashCommon.float2int(key) ^ (value); }

	 @Override
	 public boolean equals(final Object o) {
	  if (o == this) return true;
	  if (! (o instanceof Map)) return false;

	  Map<?,?> m = (Map<?,?>)o;
	  if (m.size() != 1) return false;
	  return m.entrySet().iterator().next().equals(entrySet().iterator().next());
	 }

	 @Override
	 public String toString() { return "{" + key + "=>" + value + "}"; }
	}

	/** Returns a type-specific immutable map containing only the specified pair. The returned map is serializable and cloneable.
	 *
	 * <p>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned map.
	 * @param value the only value of the returned map.
	 * @return a type-specific immutable map containing just the pair {@code &lt;key,value&gt;}.
	 */

	public static Float2ByteMap singleton(final float key, byte value) { return new Singleton (key, value); }



	/** Returns a type-specific immutable map containing only the specified pair. The returned map is serializable and cloneable.
	 *
	 * <p>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned map.
	 * @param value the only value of the returned map.
	 * @return a type-specific immutable map containing just the pair {@code &lt;key,value&gt;}.
	 */

	public static Float2ByteMap singleton(final Float key, final Byte value) { return new Singleton ((key).floatValue(), (value).byteValue()); }




	/** A synchronized wrapper class for maps. */

	public static class SynchronizedMap extends Float2ByteFunctions.SynchronizedFunction implements Float2ByteMap , java.io.Serializable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected final Float2ByteMap map;

	 protected transient ObjectSet<Float2ByteMap.Entry > entries;
	 protected transient FloatSet keys;
	 protected transient ByteCollection values;

	 protected SynchronizedMap(final Float2ByteMap m, final Object sync) {
	  super(m, sync);
	  this.map = m;
	 }

	 protected SynchronizedMap(final Float2ByteMap m) {
	  super(m);
	  this.map = m;
	 }

	 @Override
	 public boolean containsValue(final byte v) { synchronized(sync) { return map.containsValue(v); } }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public boolean containsValue(final Object ov) { synchronized(sync) { return map.containsValue(ov); } }


	 @Override
	 public void putAll(final Map<? extends Float, ? extends Byte> m) { synchronized(sync) { map.putAll(m); } }

	 @Override
	 public ObjectSet<Float2ByteMap.Entry > float2ByteEntrySet() { synchronized(sync) { if (entries == null) entries = ObjectSets.synchronize(map.float2ByteEntrySet(), sync); return entries; } }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated



	 @Override
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public ObjectSet<Map.Entry<Float, Byte>> entrySet() { return (ObjectSet)float2ByteEntrySet(); }

	 @Override
	 public FloatSet keySet() {
	  synchronized(sync) { if (keys == null) keys = FloatSets.synchronize(map.keySet(), sync); return keys; }
	 }

	 @Override
	 public ByteCollection values() {
	  synchronized(sync) { if (values == null) return ByteCollections.synchronize(map.values(), sync); return values; }
	 }

	 @Override
	 public boolean isEmpty() { synchronized(sync) { return map.isEmpty(); } }

	 @Override
	 public int hashCode() { synchronized(sync) { return map.hashCode(); } }

	 @Override
	 public boolean equals(final Object o) {
	  if (o == this) return true;
	  synchronized(sync) { return map.equals(o); }
	 }

	 private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
	  synchronized(sync) { s.defaultWriteObject(); }
	 }

	 // Defaultable methods

	 @Override
	 public byte getOrDefault(final float key, final byte defaultValue) { synchronized(sync) { return map.getOrDefault(key, defaultValue); } }

	 @Override
	 public void forEach(final java.util.function.BiConsumer<? super Float, ? super Byte> action) { synchronized (sync) { map.forEach(action); } }

	 @Override
	 public void replaceAll(final java.util.function.BiFunction<? super Float, ? super Byte, ? extends Byte> function) { synchronized (sync) { map.replaceAll(function); } }

	 @Override
	 public byte putIfAbsent(final float key, final byte value) { synchronized(sync) { return map.putIfAbsent(key, value); } }

	 @Override
	 public boolean remove(final float key, final byte value) { synchronized(sync) { return map.remove(key, value); } }

	 @Override
	 public byte replace(final float key, final byte value) { synchronized(sync) { return map.replace(key, value); } }

	 @Override
	 public boolean replace(final float key, final byte oldValue, final byte newValue) { synchronized(sync) { return map.replace(key, oldValue, newValue); } }


	 @Override
	 public byte computeIfAbsent(final float key, final java.util.function.DoubleToIntFunction mappingFunction) { synchronized(sync) { return map.computeIfAbsent(key, mappingFunction); } }



	 @Override
	 public byte computeIfAbsentNullable(final float key, final java.util.function.DoubleFunction<? extends Byte> mappingFunction) { synchronized(sync) { return map.computeIfAbsentNullable(key, mappingFunction); } }



	 @Override
	 public byte computeIfAbsentPartial(final float key, final Float2ByteFunction mappingFunction) { synchronized(sync) { return map.computeIfAbsentPartial(key, mappingFunction); } }


	 @Override
	 public byte computeIfPresent(final float key, final java.util.function.BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) {
	  synchronized (sync) { return map.computeIfPresent(key, remappingFunction); }
	 }

	 @Override
	 public byte compute(final float key, final java.util.function.BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) {
	  synchronized (sync) { return map.compute(key, remappingFunction); }
	 }

	 @Override
	 public byte merge(final float key, final byte value, final java.util.function.BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
	  synchronized (sync) { return map.merge(key, value, remappingFunction); }
	 }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Byte getOrDefault(final Object key, final Byte defaultValue) { synchronized (sync) { return map.getOrDefault(key, defaultValue); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public boolean remove(final Object key, final Object value) { synchronized (sync) { return map.remove(key, value); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Byte replace(final Float key, final Byte value) { synchronized (sync) { return map.replace(key, value); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public boolean replace(final Float key, final Byte oldValue, final Byte newValue) { synchronized (sync) { return map.replace(key, oldValue, newValue); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Byte putIfAbsent(final Float key, final Byte value) { synchronized (sync) { return map.putIfAbsent(key, value); } }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated

	 @Override
	 public Byte computeIfAbsent(final Float key, final java.util.function.Function<? super Float, ? extends Byte> mappingFunction) { synchronized (sync) { return map.computeIfAbsent(key, mappingFunction); } }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated

	 @Override
	 public Byte computeIfPresent(final Float key, final java.util.function.BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) { synchronized (sync) { return map.computeIfPresent(key, remappingFunction); } }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated

	 @Override
	 public Byte compute(final Float key, final java.util.function.BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) { synchronized (sync) { return map.compute(key, remappingFunction); } }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Byte merge(final Float key, final Byte value, final java.util.function.BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) { synchronized (sync) { return map.merge(key, value, remappingFunction); } }


	}

	/** Returns a synchronized type-specific map backed by the given type-specific map.
	 *
	 * @param m the map to be wrapped in a synchronized map.
	 * @return a synchronized view of the specified map.
	 * @see java.util.Collections#synchronizedMap(Map)
	 */
	public static Float2ByteMap synchronize(final Float2ByteMap m) { return new SynchronizedMap (m); }

	/** Returns a synchronized type-specific map backed by the given type-specific map, using an assigned object to synchronize.
	 *
	 * @param m the map to be wrapped in a synchronized map.
	 * @param sync an object that will be used to synchronize the access to the map.
	 * @return a synchronized view of the specified map.
	 * @see java.util.Collections#synchronizedMap(Map)
	 */

	public static Float2ByteMap synchronize(final Float2ByteMap m, final Object sync) { return new SynchronizedMap (m, sync); }



	/** An unmodifiable wrapper class for maps. */

	public static class UnmodifiableMap extends Float2ByteFunctions.UnmodifiableFunction implements Float2ByteMap , java.io.Serializable {

	 private static final long serialVersionUID = -7046029254386353129L;

	 protected final Float2ByteMap map;

	 protected transient ObjectSet<Float2ByteMap.Entry > entries;
	 protected transient FloatSet keys;
	 protected transient ByteCollection values;

	 protected UnmodifiableMap(final Float2ByteMap m) {
	  super(m);
	  this.map = m;
	 }

	 @Override
	 public boolean containsValue(final byte v) { return map.containsValue(v); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public boolean containsValue(final Object ov) { return map.containsValue(ov); }


	 @Override
	 public void putAll(final Map<? extends Float, ? extends Byte> m) { throw new UnsupportedOperationException(); }

	 @Override
	 public ObjectSet<Float2ByteMap.Entry > float2ByteEntrySet() { if (entries == null) entries = ObjectSets.unmodifiable(map.float2ByteEntrySet()); return entries; }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated



	 @Override
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public ObjectSet<Map.Entry<Float, Byte>> entrySet() { return (ObjectSet)float2ByteEntrySet(); }

	 @Override
	 public FloatSet keySet() { if (keys == null) keys = FloatSets.unmodifiable(map.keySet()); return keys; }

	 @Override
	 public ByteCollection values() { if (values == null) return ByteCollections.unmodifiable(map.values()); return values; }

	 @Override
	 public boolean isEmpty() { return map.isEmpty(); }

	 @Override
	 public int hashCode() { return map.hashCode(); }

	 @Override
	 public boolean equals(final Object o) {
	  if (o == this) return true;
	  return map.equals(o);
	 }

	 // Defaultable methods

	 @Override
	 public byte getOrDefault(final float key, final byte defaultValue) { return map.getOrDefault(key, defaultValue); }

	 @Override
	 public void forEach(final java.util.function.BiConsumer<? super Float, ? super Byte> action) { map.forEach(action); }

	 @Override
	 public void replaceAll(final java.util.function.BiFunction<? super Float, ? super Byte, ? extends Byte> function) { throw new UnsupportedOperationException(); }

	 @Override
	 public byte putIfAbsent(final float key, final byte value) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean remove(final float key, final byte value) { throw new UnsupportedOperationException(); }

	 @Override
	 public byte replace(final float key, final byte value) { throw new UnsupportedOperationException(); }

	 @Override
	 public boolean replace(final float key, final byte oldValue, final byte newValue) { throw new UnsupportedOperationException(); }


	 @Override
	 public byte computeIfAbsent(final float key, final java.util.function.DoubleToIntFunction mappingFunction) { throw new UnsupportedOperationException(); }



	 @Override
	 public byte computeIfAbsentNullable(final float key, final java.util.function.DoubleFunction<? extends Byte> mappingFunction) { throw new UnsupportedOperationException(); }



	 @Override
	 public byte computeIfAbsentPartial(final float key, final Float2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }


	 @Override
	 public byte computeIfPresent(final float key, final java.util.function.BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) { throw new UnsupportedOperationException(); }

	 @Override
	 public byte compute(final float key, final java.util.function.BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) { throw new UnsupportedOperationException(); }

	 @Override
	 public byte merge(final float key, final byte value, final java.util.function.BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) { throw new UnsupportedOperationException(); }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Byte getOrDefault(final Object key, final Byte defaultValue) { return map.getOrDefault(key, defaultValue); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public boolean remove(final Object key, final Object value) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Byte replace(final Float key, final Byte value) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public boolean replace(final Float key, final Byte oldValue, final Byte newValue) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Byte putIfAbsent(final Float key, final Byte value) { throw new UnsupportedOperationException(); }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated

	 @Override
	 public Byte computeIfAbsent(final Float key, final java.util.function.Function<? super Float, ? extends Byte> mappingFunction) { throw new UnsupportedOperationException(); }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated

	 @Override
	 public Byte computeIfPresent(final Float key, final java.util.function.BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) { throw new UnsupportedOperationException(); }


	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated

	 @Override
	 public Byte compute(final Float key, final java.util.function.BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) { throw new UnsupportedOperationException(); }

	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Byte merge(final Float key, final Byte value, final java.util.function.BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) { throw new UnsupportedOperationException(); }


	}

	/** Returns an unmodifiable type-specific map backed by the given type-specific map.
	 *
	 * @param m the map to be wrapped in an unmodifiable map.
	 * @return an unmodifiable view of the specified map.
	 * @see java.util.Collections#unmodifiableMap(Map)
	 */
	public static Float2ByteMap unmodifiable(final Float2ByteMap m) { return new UnmodifiableMap (m); }

}

