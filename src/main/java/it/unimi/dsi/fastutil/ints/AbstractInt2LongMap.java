
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


package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongIterator;
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

public abstract class AbstractInt2LongMap extends AbstractInt2LongFunction implements Int2LongMap , java.io.Serializable {

	private static final long serialVersionUID = -4940583368468432370L;

	protected AbstractInt2LongMap() {}

	@Override
	public boolean containsValue(final long v) {
	 return values().contains(v);
	}

	@Override
	public boolean containsKey(final int k) {
	 final ObjectIterator<Int2LongMap.Entry > i = int2LongEntrySet().iterator();
	 while(i.hasNext())
	  if (i.next().getIntKey() == k)
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

	public static class BasicEntry implements Int2LongMap.Entry {
	 protected int key;
	 protected long value;

	 public BasicEntry() {}

	 public BasicEntry(final Integer key, final Long value) {
	  this.key = (key).intValue();
	  this.value = (value).longValue();
	 }



	 public BasicEntry(final int key, final long value) {
	  this.key = key;
	  this.value = value;
	 }



	 @Override
	 public int getIntKey() {
	  return key;
	 }

	 @Override
	 public long getLongValue() {
	  return value;
	 }

	 @Override
	 public long setValue(final long value) {
	  throw new UnsupportedOperationException();
	 }


	 @Override
	 public boolean equals(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;
	  if (o instanceof Int2LongMap.Entry) {
	   final Int2LongMap.Entry e = (Int2LongMap.Entry ) o;
	   return ( (key) == (e.getIntKey()) ) && ( (value) == (e.getLongValue()) );
	  }

	  final Map.Entry<?,?> e = (Map.Entry<?,?>)o;
	  final Object key = e.getKey();

	  if (key == null || !(key instanceof Integer)) return false;

	  final Object value = e.getValue();

	  if (value == null || !(value instanceof Long)) return false;

	  return ( (this.key) == (((Integer)(key)).intValue()) ) && ( (this.value) == (((Long)(value)).longValue()) );
	 }

	 @Override
	 public int hashCode() {
	  return (key) ^ it.unimi.dsi.fastutil.HashCommon.long2int(value);
	 }

	 @Override
	 public String toString() {
	  return key + "->" + value;
	 }
	}

	/** This class provides a basic implementation for an Entry set which forwards some queries to the map.
	 */

	public abstract static class BasicEntrySet extends AbstractObjectSet<Entry > {
	 protected final Int2LongMap map;

	 public BasicEntrySet(final Int2LongMap map) {
	  this.map = map;
	 }


	 @Override
	 public boolean contains(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;

	  if (o instanceof Int2LongMap.Entry) {
	   final Int2LongMap.Entry e = (Int2LongMap.Entry ) o;
	   final int k = e.getIntKey();
	   return map.containsKey(k) && ( (map.get(k)) == (e.getLongValue()) );
	  }

	  final Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;


	  final Object key = e.getKey();
	  if (key == null || !(key instanceof Integer)) return false;
	  final int k = ((Integer)(key)).intValue();



	  final Object value = e.getValue();

	  if (value == null || !(value instanceof Long)) return false;


	  return map.containsKey(k) && ( (map.get(k)) == (((Long)(value)).longValue()) );
	 }


	 @Override
	 public boolean remove(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;

	  if (o instanceof Int2LongMap.Entry) {
	   final Int2LongMap.Entry e = (Int2LongMap.Entry ) o;
	   return map.remove(e.getIntKey(), e.getLongValue());
	  }

	  Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;


	  final Object key = e.getKey();
	  if (key == null || !(key instanceof Integer)) return false;
	  final int k = ((Integer)(key)).intValue();




	  final Object value = e.getValue();
	  if (value == null || !(value instanceof Long)) return false;
	  final long v = ((Long)(value)).longValue();




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
	public IntSet keySet() {
	 return new AbstractIntSet () {
	   @Override
	   public boolean contains(final int k) { return containsKey(k); }
	   @Override
	   public int size() { return AbstractInt2LongMap.this.size(); }
	   @Override
	   public void clear() { AbstractInt2LongMap.this.clear(); }
	   @Override
	   public IntIterator iterator() {
	    return new IntIterator () {
	      private final ObjectIterator<Int2LongMap.Entry > i = Int2LongMaps.fastIterator(AbstractInt2LongMap.this);
	      @Override
	      public int nextInt() { return i.next().getIntKey(); };
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
	public LongCollection values() {
	 return new AbstractLongCollection () {
	   @Override
	   public boolean contains(final long k) { return containsValue(k); }
	   @Override
	   public int size() { return AbstractInt2LongMap.this.size(); }
	   @Override
	   public void clear() { AbstractInt2LongMap.this.clear(); }

	   @Override
	   public LongIterator iterator() {
	    return new LongIterator () {
	      private final ObjectIterator<Int2LongMap.Entry > i = Int2LongMaps.fastIterator(AbstractInt2LongMap.this);
	      @Override
	      public long nextLong() { return i.next().getLongValue(); }
	      @Override
	      public boolean hasNext() { return i.hasNext(); }
	     };
	   }
	  };
	}

	/** {@inheritDoc} */
	@SuppressWarnings({"unchecked","deprecation"})
	@Override
	public void putAll(final Map<? extends Integer,? extends Long> m) {
	 if (m instanceof Int2LongMap) {
	  ObjectIterator<Int2LongMap.Entry > i = Int2LongMaps.fastIterator((Int2LongMap ) m);

	  while (i.hasNext()) {
	   final Int2LongMap.Entry e = i.next();
	   put(e.getIntKey(), e.getLongValue());
	  }
	 } else {
	  int n = m.size();
	  final Iterator<? extends Map.Entry<? extends Integer,? extends Long>> i = m.entrySet().iterator();
	  Map.Entry<? extends Integer,? extends Long> e;
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
	 final ObjectIterator<Int2LongMap.Entry > i = Int2LongMaps.fastIterator(this);

	 while(n-- != 0) h += i.next().hashCode();
	 return h;
	}

	@Override
	public boolean equals(Object o) {
	 if (o == this) return true;
	 if (! (o instanceof Map)) return false;

	 final Map<?,?> m = (Map<?,?>)o;
	 if (m.size() != size()) return false;
	 return int2LongEntrySet().containsAll(m.entrySet());
	}

	@Override
	public String toString() {
	 final StringBuilder s = new StringBuilder();
	 final ObjectIterator<Int2LongMap.Entry > i = Int2LongMaps.fastIterator(this);
	 int n = size();
	 Int2LongMap.Entry e;
	 boolean first = true;

	 s.append("{");

	 while(n-- != 0) {
	  if (first) first = false;
	  else s.append(", ");

	  e = i.next();




	   s.append(String.valueOf(e.getIntKey()));
	  s.append("=>");



	   s.append(String.valueOf(e.getLongValue()));
	 }

	 s.append("}");
	 return s.toString();
	}
}

