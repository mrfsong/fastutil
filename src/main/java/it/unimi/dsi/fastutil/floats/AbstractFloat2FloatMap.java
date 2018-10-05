
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

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Iterator;
import java.util.Map;

/** An abstract class providing basic methods for maps implementing a type-specific interface.
	*
	* <p>Optional operations just throw an {@link
	* UnsupportedOperationException}. Generic versions of accessors delegate to
	* the corresponding type-specific counterparts following the interface rules
	* (they take care of returning {@code null} on a missing key).
	*
	* <p>As a further help, this class provides a {@link BasicEntry BasicEntry} inner class
	* that implements a type-specific version of {@link java.util.Map.Entry}; it
	* is particularly useful for those classes that do not implement their own
	* entries (e.g., most immutable maps).
	*/

public abstract class AbstractFloat2FloatMap extends AbstractFloat2FloatFunction implements Float2FloatMap , java.io.Serializable {

	private static final long serialVersionUID = -4940583368468432370L;

	protected AbstractFloat2FloatMap() {}

	@Override
	public boolean containsValue(final float v) {
	 return values().contains(v);
	}

	@Override
	public boolean containsKey(final float k) {
	 final ObjectIterator<Float2FloatMap.Entry > i = float2FloatEntrySet().iterator();
	 while(i.hasNext())
	  if (i.next().getFloatKey() == k)
	   return true;

	 return false;
	}

	@Override
	public boolean isEmpty() {
	 return size() == 0;
	}

	/** This class provides a basic but complete type-specific entry class for all those maps implementations
	 * that do not have entries on their own (e.g., most immutable maps).
	 *
	 * <p>This class does not implement {@link java.util.Map.Entry#setValue(Object) setValue()}, as the modification
	 * would not be reflected in the base map.
	 */

	public static class BasicEntry implements Float2FloatMap.Entry {
	 protected float key;
	 protected float value;

	 public BasicEntry() {}

	 public BasicEntry(final Float key, final Float value) {
	  this.key = (key).floatValue();
	  this.value = (value).floatValue();
	 }



	 public BasicEntry(final float key, final float value) {
	  this.key = key;
	  this.value = value;
	 }



	 @Override
	 public float getFloatKey() {
	  return key;
	 }

	 @Override
	 public float getFloatValue() {
	  return value;
	 }

	 @Override
	 public float setValue(final float value) {
	  throw new UnsupportedOperationException();
	 }


	 @Override
	 public boolean equals(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;
	  if (o instanceof Float2FloatMap.Entry) {
	   final Float2FloatMap.Entry e = (Float2FloatMap.Entry ) o;
	   return ( Float.floatToIntBits(key) == Float.floatToIntBits(e.getFloatKey()) ) && ( Float.floatToIntBits(value) == Float.floatToIntBits(e.getFloatValue()) );
	  }

	  final Map.Entry<?,?> e = (Map.Entry<?,?>)o;
	  final Object key = e.getKey();

	  if (key == null || !(key instanceof Float)) return false;

	  final Object value = e.getValue();

	  if (value == null || !(value instanceof Float)) return false;

	  return ( Float.floatToIntBits(this.key) == Float.floatToIntBits(((Float)(key)).floatValue()) ) && ( Float.floatToIntBits(this.value) == Float.floatToIntBits(((Float)(value)).floatValue()) );
	 }

	 @Override
	 public int hashCode() {
	  return it.unimi.dsi.fastutil.HashCommon.float2int(key) ^ it.unimi.dsi.fastutil.HashCommon.float2int(value);
	 }

	 @Override
	 public String toString() {
	  return key + "->" + value;
	 }
	}

	/** This class provides a basic implementation for an Entry set which forwards some queries to the map.
	 */

	public abstract static class BasicEntrySet extends AbstractObjectSet<Entry > {
	 protected final Float2FloatMap map;

	 public BasicEntrySet(final Float2FloatMap map) {
	  this.map = map;
	 }


	 @Override
	 public boolean contains(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;

	  if (o instanceof Float2FloatMap.Entry) {
	   final Float2FloatMap.Entry e = (Float2FloatMap.Entry ) o;
	   final float k = e.getFloatKey();
	   return map.containsKey(k) && ( Float.floatToIntBits(map.get(k)) == Float.floatToIntBits(e.getFloatValue()) );
	  }

	  final Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;


	  final Object key = e.getKey();
	  if (key == null || !(key instanceof Float)) return false;
	  final float k = ((Float)(key)).floatValue();



	  final Object value = e.getValue();

	  if (value == null || !(value instanceof Float)) return false;


	  return map.containsKey(k) && ( Float.floatToIntBits(map.get(k)) == Float.floatToIntBits(((Float)(value)).floatValue()) );
	 }


	 @Override
	 public boolean remove(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;

	  if (o instanceof Float2FloatMap.Entry) {
	   final Float2FloatMap.Entry e = (Float2FloatMap.Entry ) o;
	   return map.remove(e.getFloatKey(), e.getFloatValue());
	  }

	  Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;


	  final Object key = e.getKey();
	  if (key == null || !(key instanceof Float)) return false;
	  final float k = ((Float)(key)).floatValue();




	  final Object value = e.getValue();
	  if (value == null || !(value instanceof Float)) return false;
	  final float v = ((Float)(value)).floatValue();




	  return map.remove(k, v);
	 }

	 @Override
	 public int size() {
	  return map.size();
	 }
	}


	/** Returns a type-specific-set view of the keys of this map.
	 *
	 * <p>The view is backed by the set returned by {@link Map#entrySet()}. Note that
	 * <em>no attempt is made at caching the result of this method</em>, as this would
	 * require adding some attributes that lightweight implementations would
	 * not need. Subclasses may easily override this policy by calling
	 * this method and caching the result, but implementors are encouraged to
	 * write more efficient ad-hoc implementations.
	 *
	 * @return a set view of the keys of this map; it may be safely cast to a type-specific interface.
	 */
	@Override
	public FloatSet keySet() {
	 return new AbstractFloatSet () {
	   @Override
	   public boolean contains(final float k) { return containsKey(k); }
	   @Override
	   public int size() { return AbstractFloat2FloatMap.this.size(); }
	   @Override
	   public void clear() { AbstractFloat2FloatMap.this.clear(); }
	   @Override
	   public FloatIterator iterator() {
	    return new FloatIterator () {
	      private final ObjectIterator<Float2FloatMap.Entry > i = Float2FloatMaps.fastIterator(AbstractFloat2FloatMap.this);
	      @Override
	      public float nextFloat() { return i.next().getFloatKey(); };
	      @Override
	      public boolean hasNext() { return i.hasNext(); }
	      @Override
	      public void remove() { i.remove(); }
	     };
	   }
	  };
	}

	/** Returns a type-specific-set view of the values of this map.
	 *
	 * <p>The view is backed by the set returned by {@link Map#entrySet()}. Note that
	 * <em>no attempt is made at caching the result of this method</em>, as this would
	 * require adding some attributes that lightweight implementations would
	 * not need. Subclasses may easily override this policy by calling
	 * this method and caching the result, but implementors are encouraged to
	 * write more efficient ad-hoc implementations.
	 *
	 * @return a set view of the values of this map; it may be safely cast to a type-specific interface.
	 */
	@Override
	public FloatCollection values() {
	 return new AbstractFloatCollection () {
	   @Override
	   public boolean contains(final float k) { return containsValue(k); }
	   @Override
	   public int size() { return AbstractFloat2FloatMap.this.size(); }
	   @Override
	   public void clear() { AbstractFloat2FloatMap.this.clear(); }

	   @Override
	   public FloatIterator iterator() {
	    return new FloatIterator () {
	      private final ObjectIterator<Float2FloatMap.Entry > i = Float2FloatMaps.fastIterator(AbstractFloat2FloatMap.this);
	      @Override
	      public float nextFloat() { return i.next().getFloatValue(); }
	      @Override
	      public boolean hasNext() { return i.hasNext(); }
	     };
	   }
	  };
	}

	/** {@inheritDoc} */
	@SuppressWarnings({"unchecked","deprecation"})
	@Override
	public void putAll(final Map<? extends Float,? extends Float> m) {
	 if (m instanceof Float2FloatMap) {
	  ObjectIterator<Float2FloatMap.Entry > i = Float2FloatMaps.fastIterator((Float2FloatMap ) m);

	  while (i.hasNext()) {
	   final Float2FloatMap.Entry e = i.next();
	   put(e.getFloatKey(), e.getFloatValue());
	  }
	 } else {
	  int n = m.size();
	  final Iterator<? extends Map.Entry<? extends Float,? extends Float>> i = m.entrySet().iterator();
	  Map.Entry<? extends Float,? extends Float> e;
	  while (n-- != 0) {
	   e = i.next();
	   put(e.getKey(), e.getValue());
	  }
	 }
	}

	/** Returns a hash code for this map.
	 *
	 * The hash code of a map is computed by summing the hash codes of its entries.
	 *
	 * @return a hash code for this map.
	 */
	@Override
	public int hashCode() {
	 int h = 0, n = size();
	 final ObjectIterator<Float2FloatMap.Entry > i = Float2FloatMaps.fastIterator(this);

	 while(n-- != 0) h += i.next().hashCode();
	 return h;
	}

	@Override
	public boolean equals(Object o) {
	 if (o == this) return true;
	 if (! (o instanceof Map)) return false;

	 final Map<?,?> m = (Map<?,?>)o;
	 if (m.size() != size()) return false;
	 return float2FloatEntrySet().containsAll(m.entrySet());
	}

	@Override
	public String toString() {
	 final StringBuilder s = new StringBuilder();
	 final ObjectIterator<Float2FloatMap.Entry > i = Float2FloatMaps.fastIterator(this);
	 int n = size();
	 Float2FloatMap.Entry e;
	 boolean first = true;

	 s.append("{");

	 while(n-- != 0) {
	  if (first) first = false;
	  else s.append(", ");

	  e = i.next();




	   s.append(String.valueOf(e.getFloatKey()));
	  s.append("=>");



	   s.append(String.valueOf(e.getFloatValue()));
	 }

	 s.append("}");
	 return s.toString();
	}
}

