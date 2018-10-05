
/*
	* Copyright (C) 2007-2017 Sebastiano Vigna
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


package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortArrays;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

import java.util.Map;
import java.util.NoSuchElementException;

/** A simple, brute-force implementation of a map based on two parallel backing arrays.
	*
	* <p>The main purpose of this
	* implementation is that of wrapping cleanly the brute-force approach to the storage of a very
	* small number of pairs: just put them into two parallel arrays and scan linearly to find an item.
	*/

public class Object2ShortArrayMap <K> extends AbstractObject2ShortMap <K> implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	/** The keys (valid up to {@link #size}, excluded). */
	private transient Object[] key;
	/** The values (parallel to {@link #key}). */
	private transient short[] value;
	/** The number of valid entries in {@link #key} and {@link #value}. */
	private int size;

	/** Creates a new empty array map with given key and value backing arrays. The resulting map will have as many entries as the given arrays.
	 *
	 * <p>It is responsibility of the caller that the elements of {@code key} are distinct.
	 *
	 * @param key the key array.
	 * @param value the value array (it <em>must</em> have the same length as {@code key}).
	 */
	public Object2ShortArrayMap(final Object[] key, final short[] value) {
	 this.key = key;
	 this.value = value;
	 size = key.length;
	 if(key.length != value.length) throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
	}

	/** Creates a new empty array map.
	 */
	public Object2ShortArrayMap() {
	 this.key = ObjectArrays.EMPTY_ARRAY;
	 this.value = ShortArrays.EMPTY_ARRAY;
	}

	/** Creates a new empty array map of given capacity.
	 *
	 * @param capacity the initial capacity.
	 */
	public Object2ShortArrayMap(final int capacity) {
	 this.key = new Object[capacity];
	 this.value = new short[capacity];
	}

	/** Creates a new empty array map copying the entries of a given map.
	 *
	 * @param m a map.
	 */
	public Object2ShortArrayMap(final Object2ShortMap <K> m) {
	 this(m.size());
	 putAll(m);
	}

	/** Creates a new empty array map copying the entries of a given map.
	 *
	 * @param m a map.
	 */
	public Object2ShortArrayMap(final Map<? extends K, ? extends Short> m) {
	 this(m.size());
	 putAll(m);
	}

	/** Creates a new array map with given key and value backing arrays, using the given number of elements.
	 *
	 * <p>It is responsibility of the caller that the first {@code size} elements of {@code key} are distinct.
	 *
	 * @param key the key array.
	 * @param value the value array (it <em>must</em> have the same length as {@code key}).
	 * @param size the number of valid elements in {@code key} and {@code value}.
	 */
	public Object2ShortArrayMap(final Object[] key, final short[] value, final int size) {
	 this.key = key;
	 this.value = value;
	 this.size = size;
	 if(key.length != value.length) throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
	 if (size > key.length) throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the backing-arrays size (" + key.length + ")");
	}

	private final class EntrySet extends AbstractObjectSet<Object2ShortMap.Entry <K> > implements FastEntrySet <K> {

	 @Override
	 public ObjectIterator<Object2ShortMap.Entry <K> > iterator() {
	  return new ObjectIterator<Object2ShortMap.Entry <K> >() {
	   int curr = -1, next = 0;

	   @Override
	   public boolean hasNext() { return next < size; }

	   @Override
	   @SuppressWarnings("unchecked")
	   public Entry <K> next() {
	    if (! hasNext()) throw new NoSuchElementException();
	    return new AbstractObject2ShortMap.BasicEntry <>((K) key[curr = next], value[next++]);
	   }

	   @Override
	   public void remove() {
	    if (curr == -1) throw new IllegalStateException();
	    curr = -1;
	    final int tail = size-- - next--;
	    System.arraycopy(key, next + 1, key, next, tail);
	    System.arraycopy(value, next + 1, value, next, tail);

	    key[size] = null;




	   }
	  };
	 }

	 @Override
	 public ObjectIterator<Object2ShortMap.Entry <K> > fastIterator() {
	  return new ObjectIterator<Object2ShortMap.Entry <K> >() {
	   int next = 0, curr = -1;
	   final BasicEntry <K> entry = new BasicEntry <> ();

	   @Override
	   public boolean hasNext() { return next < size; }

	   @Override
	   @SuppressWarnings("unchecked")
	   public Entry <K> next() {
	    if (! hasNext()) throw new NoSuchElementException();
	    entry.key = (K) key[curr = next];
	    entry.value = value[next++];
	    return entry;
	   }

	   @Override
	   public void remove() {
	    if (curr == -1) throw new IllegalStateException();
	    curr = -1;
	    final int tail = size-- - next--;
	    System.arraycopy(key, next + 1, key, next, tail);
	    System.arraycopy(value, next + 1, value, next, tail);

	    key[size] = null;




	   }
	  };
	 }

	 @Override
	 public int size() { return size; }

	 @Override
	 @SuppressWarnings("unchecked")
	 public boolean contains(Object o) {
	  if (! (o instanceof Map.Entry)) return false;
	  final Map.Entry<?,?> e = (Map.Entry<?,?>)o;




	  if (e.getValue() == null || ! (e.getValue() instanceof Short)) return false;

	  final K k = ((K) e.getKey());
	  return Object2ShortArrayMap.this.containsKey(k) && ( (Object2ShortArrayMap.this.getShort(k)) == (((Short)(e.getValue())).shortValue()) );
	 }

	 @Override
	 @SuppressWarnings("unchecked")
	 public boolean remove(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;
	  final Map.Entry<?,?> e = (Map.Entry<?,?>)o;




	  if (e.getValue() == null || ! (e.getValue() instanceof Short)) return false;

	  final K k = ((K) e.getKey());
	  final short v = ((Short)(e.getValue())).shortValue();

	  final int oldPos = Object2ShortArrayMap.this.findKey(k);
	  if (oldPos == -1 || ! ( (v) == (Object2ShortArrayMap.this.value[oldPos]) )) return false;
	  final int tail = size - oldPos - 1;
	  System.arraycopy(Object2ShortArrayMap.this.key, oldPos + 1, Object2ShortArrayMap.this.key, oldPos, tail);
	  System.arraycopy(Object2ShortArrayMap.this.value, oldPos + 1, Object2ShortArrayMap.this.value, oldPos, tail);
	  Object2ShortArrayMap.this.size--;

	  Object2ShortArrayMap.this.key[size] = null;




	  return true;
	 }
	}

	@Override
	public FastEntrySet <K> object2ShortEntrySet() { return new EntrySet(); }

	private int findKey(final Object k) {
	 final Object[] key = this.key;
	 for(int i = size; i-- != 0;) if (java.util.Objects.equals(key[i], k)) return i;
	 return -1;
	}

	@Override

	public short getShort(final Object k) {
	 final Object[] key = this.key;
	 for(int i = size; i-- != 0;) if (java.util.Objects.equals(key[i], k)) return value[i];
	 return defRetValue;
	}

	@Override
	public int size() { return size; }

	@Override
	public void clear() {

	 for(int i = size; i-- != 0;) {

	  key[i] = null;




	 }

	 size = 0;
	}

	@Override
	public boolean containsKey(final Object k) { return findKey(k) != -1; }

	@Override
	public boolean containsValue(short v) {
	 for(int i = size; i-- != 0;) if (( (value[i]) == (v) )) return true;
	 return false;
	}

	@Override
	public boolean isEmpty() { return size == 0; }

	@Override

	public short put(K k, short v) {
	 final int oldKey = findKey(k);
	 if (oldKey != -1) {
	  final short oldValue = value[oldKey];
	  value[oldKey] = v;
	  return oldValue;
	 }
	 if (size == key.length) {
	  final Object[] newKey = new Object[size == 0 ? 2 : size * 2];
	  final short[] newValue = new short[size == 0 ? 2 : size * 2];
	  for(int i = size; i-- != 0;) {
	   newKey[i] = key[i];
	   newValue[i] = value[i];
	  }
	  key = newKey;
	  value = newValue;
	 }
	 key[size] = k;
	 value[size] = v;
	 size++;
	 return defRetValue;
	}

	@Override

	public short removeShort(final Object k) {
	 final int oldPos = findKey(k);
	 if (oldPos == -1) return defRetValue;
	 final short oldValue = value[oldPos];
	 final int tail = size - oldPos - 1;
	 System.arraycopy(key, oldPos + 1, key, oldPos, tail);
	 System.arraycopy(value, oldPos + 1, value, oldPos, tail);
	 size--;

	 key[size] = null;




	 return oldValue;
	}

	@Override
	public ObjectSet <K> keySet() {
	 return new AbstractObjectSet <K>() {
	  @Override
	  public boolean contains(final Object k) {
	   return findKey(k) != -1;
	  }

	  @Override
	  public boolean remove(final Object k) {
	    final int oldPos = findKey(k);
	    if (oldPos == -1) return false;
	    final int tail = size - oldPos - 1;
	    System.arraycopy(key, oldPos + 1, key, oldPos, tail);
	    System.arraycopy(value, oldPos + 1, value, oldPos, tail);
	    size--;
	    return true;
	  }

	  @Override
	  public ObjectIterator <K> iterator() {
	   return new ObjectIterator <K>() {
	    int pos = 0;
	    @Override
	    public boolean hasNext() {
	     return pos < size;
	    }

	    @Override
	    @SuppressWarnings("unchecked")
	    public K next() {
	     if (! hasNext()) throw new NoSuchElementException();
	     return (K) key[pos++];
	    }

	    @Override
	    public void remove() {
	     if (pos == 0) throw new IllegalStateException();
	     final int tail = size - pos;
	     System.arraycopy(key, pos, key, pos - 1, tail);
	     System.arraycopy(value, pos, value, pos - 1, tail);
	     size--;
	    }
	   };
	  }

	  @Override
	  public int size() {
	   return size;
	  }

	  @Override
	  public void clear() {
	   Object2ShortArrayMap.this.clear();
	  }
	 };
	}

	@Override
	public ShortCollection values() {
	 return new AbstractShortCollection () {

	  @Override
	  public boolean contains(final short v) {
	   return containsValue(v);
	  }

	  @Override
	  public it.unimi.dsi.fastutil.shorts.ShortIterator iterator() {
	   return new it.unimi.dsi.fastutil.shorts.ShortIterator () {
	    int pos = 0;
	    @Override
	    public boolean hasNext() {
	     return pos < size;
	    }

	    @Override

	    public short nextShort() {
	     if (! hasNext()) throw new NoSuchElementException();
	     return value[pos++];
	    }

	    @Override
	    public void remove() {
	     if (pos == 0) throw new IllegalStateException();
	     final int tail = size - pos;
	     System.arraycopy(key, pos, key, pos - 1, tail);
	     System.arraycopy(value, pos, value, pos - 1, tail);
	     size--;
	    }
	   };
	  }

	  @Override
	  public int size() {
	   return size;
	  }

	  @Override
	  public void clear() {
	   Object2ShortArrayMap.this.clear();
	  }
	 };
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
	public Object2ShortArrayMap <K> clone() {
	 Object2ShortArrayMap <K> c;
	 try {
	  c = (Object2ShortArrayMap <K>)super.clone();
	 }
	 catch(CloneNotSupportedException cantHappen) {
	  throw new InternalError();
	 }
	 c.key = key.clone();
	 c.value = value.clone();
	 return c;
	}

	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
	 s.defaultWriteObject();
	 for(int i = 0; i < size; i++) {
	  s.writeObject(key[i]);
	  s.writeShort(value[i]);
	 }
	}

	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
	 s.defaultReadObject();
	 key = new Object[size];
	 value = new short[size];
	 for(int i = 0; i < size; i++) {
	  key[i] = s.readObject();
	  value[i] = s.readShort();
	 }
	}
}

