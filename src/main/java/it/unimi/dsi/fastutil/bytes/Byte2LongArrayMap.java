
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


package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import it.unimi.dsi.fastutil.longs.LongArrays;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Map;
import java.util.NoSuchElementException;

/** A simple, brute-force implementation of a map based on two parallel backing arrays.
	*
	* <p>The main purpose of this
	* implementation is that of wrapping cleanly the brute-force approach to the storage of a very
	* small number of pairs: just put them into two parallel arrays and scan linearly to find an item.
	*/

public class Byte2LongArrayMap extends AbstractByte2LongMap implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	/** The keys (valid up to {@link #size}, excluded). */
	private transient byte[] key;
	/** The values (parallel to {@link #key}). */
	private transient long[] value;
	/** The number of valid entries in {@link #key} and {@link #value}. */
	private int size;

	/** Creates a new empty array map with given key and value backing arrays. The resulting map will have as many entries as the given arrays.
	 *
	 * <p>It is responsibility of the caller that the elements of {@code key} are distinct.
	 *
	 * @param key the key array.
	 * @param value the value array (it <em>must</em> have the same length as {@code key}).
	 */
	public Byte2LongArrayMap(final byte[] key, final long[] value) {
	 this.key = key;
	 this.value = value;
	 size = key.length;
	 if(key.length != value.length) throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
	}

	/** Creates a new empty array map.
	 */
	public Byte2LongArrayMap() {
	 this.key = ByteArrays.EMPTY_ARRAY;
	 this.value = LongArrays.EMPTY_ARRAY;
	}

	/** Creates a new empty array map of given capacity.
	 *
	 * @param capacity the initial capacity.
	 */
	public Byte2LongArrayMap(final int capacity) {
	 this.key = new byte[capacity];
	 this.value = new long[capacity];
	}

	/** Creates a new empty array map copying the entries of a given map.
	 *
	 * @param m a map.
	 */
	public Byte2LongArrayMap(final Byte2LongMap m) {
	 this(m.size());
	 putAll(m);
	}

	/** Creates a new empty array map copying the entries of a given map.
	 *
	 * @param m a map.
	 */
	public Byte2LongArrayMap(final Map<? extends Byte, ? extends Long> m) {
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
	public Byte2LongArrayMap(final byte[] key, final long[] value, final int size) {
	 this.key = key;
	 this.value = value;
	 this.size = size;
	 if(key.length != value.length) throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
	 if (size > key.length) throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the backing-arrays size (" + key.length + ")");
	}

	private final class EntrySet extends AbstractObjectSet<Byte2LongMap.Entry > implements FastEntrySet {

	 @Override
	 public ObjectIterator<Byte2LongMap.Entry > iterator() {
	  return new ObjectIterator<Byte2LongMap.Entry >() {
	   int curr = -1, next = 0;

	   @Override
	   public boolean hasNext() { return next < size; }

	   @Override

	   public Entry next() {
	    if (! hasNext()) throw new NoSuchElementException();
	    return new AbstractByte2LongMap.BasicEntry ( key[curr = next], value[next++]);
	   }

	   @Override
	   public void remove() {
	    if (curr == -1) throw new IllegalStateException();
	    curr = -1;
	    final int tail = size-- - next--;
	    System.arraycopy(key, next + 1, key, next, tail);
	    System.arraycopy(value, next + 1, value, next, tail);






	   }
	  };
	 }

	 @Override
	 public ObjectIterator<Byte2LongMap.Entry > fastIterator() {
	  return new ObjectIterator<Byte2LongMap.Entry >() {
	   int next = 0, curr = -1;
	   final BasicEntry entry = new BasicEntry ();

	   @Override
	   public boolean hasNext() { return next < size; }

	   @Override

	   public Entry next() {
	    if (! hasNext()) throw new NoSuchElementException();
	    entry.key = key[curr = next];
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






	   }
	  };
	 }

	 @Override
	 public int size() { return size; }

	 @Override

	 public boolean contains(Object o) {
	  if (! (o instanceof Map.Entry)) return false;
	  final Map.Entry<?,?> e = (Map.Entry<?,?>)o;

	  if (e.getKey() == null || ! (e.getKey() instanceof Byte)) return false;


	  if (e.getValue() == null || ! (e.getValue() instanceof Long)) return false;

	  final byte k = ((Byte)(e.getKey())).byteValue();
	  return Byte2LongArrayMap.this.containsKey(k) && ( (Byte2LongArrayMap.this.get(k)) == (((Long)(e.getValue())).longValue()) );
	 }

	 @Override

	 public boolean remove(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;
	  final Map.Entry<?,?> e = (Map.Entry<?,?>)o;

	  if (e.getKey() == null || ! (e.getKey() instanceof Byte)) return false;


	  if (e.getValue() == null || ! (e.getValue() instanceof Long)) return false;

	  final byte k = ((Byte)(e.getKey())).byteValue();
	  final long v = ((Long)(e.getValue())).longValue();

	  final int oldPos = Byte2LongArrayMap.this.findKey(k);
	  if (oldPos == -1 || ! ( (v) == (Byte2LongArrayMap.this.value[oldPos]) )) return false;
	  final int tail = size - oldPos - 1;
	  System.arraycopy(Byte2LongArrayMap.this.key, oldPos + 1, Byte2LongArrayMap.this.key, oldPos, tail);
	  System.arraycopy(Byte2LongArrayMap.this.value, oldPos + 1, Byte2LongArrayMap.this.value, oldPos, tail);
	  Byte2LongArrayMap.this.size--;






	  return true;
	 }
	}

	@Override
	public FastEntrySet byte2LongEntrySet() { return new EntrySet(); }

	private int findKey(final byte k) {
	 final byte[] key = this.key;
	 for(int i = size; i-- != 0;) if (( (key[i]) == (k) )) return i;
	 return -1;
	}

	@Override

	public long get(final byte k) {
	 final byte[] key = this.key;
	 for(int i = size; i-- != 0;) if (( (key[i]) == (k) )) return value[i];
	 return defRetValue;
	}

	@Override
	public int size() { return size; }

	@Override
	public void clear() {
	 size = 0;
	}

	@Override
	public boolean containsKey(final byte k) { return findKey(k) != -1; }

	@Override
	public boolean containsValue(long v) {
	 for(int i = size; i-- != 0;) if (( (value[i]) == (v) )) return true;
	 return false;
	}

	@Override
	public boolean isEmpty() { return size == 0; }

	@Override

	public long put(byte k, long v) {
	 final int oldKey = findKey(k);
	 if (oldKey != -1) {
	  final long oldValue = value[oldKey];
	  value[oldKey] = v;
	  return oldValue;
	 }
	 if (size == key.length) {
	  final byte[] newKey = new byte[size == 0 ? 2 : size * 2];
	  final long[] newValue = new long[size == 0 ? 2 : size * 2];
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

	public long remove(final byte k) {
	 final int oldPos = findKey(k);
	 if (oldPos == -1) return defRetValue;
	 final long oldValue = value[oldPos];
	 final int tail = size - oldPos - 1;
	 System.arraycopy(key, oldPos + 1, key, oldPos, tail);
	 System.arraycopy(value, oldPos + 1, value, oldPos, tail);
	 size--;






	 return oldValue;
	}

	@Override
	public ByteSet keySet() {
	 return new AbstractByteSet () {
	  @Override
	  public boolean contains(final byte k) {
	   return findKey(k) != -1;
	  }

	  @Override
	  public boolean remove(final byte k) {
	    final int oldPos = findKey(k);
	    if (oldPos == -1) return false;
	    final int tail = size - oldPos - 1;
	    System.arraycopy(key, oldPos + 1, key, oldPos, tail);
	    System.arraycopy(value, oldPos + 1, value, oldPos, tail);
	    size--;
	    return true;
	  }

	  @Override
	  public ByteIterator iterator() {
	   return new ByteIterator () {
	    int pos = 0;
	    @Override
	    public boolean hasNext() {
	     return pos < size;
	    }

	    @Override

	    public byte nextByte() {
	     if (! hasNext()) throw new NoSuchElementException();
	     return key[pos++];
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
	   Byte2LongArrayMap.this.clear();
	  }
	 };
	}

	@Override
	public LongCollection values() {
	 return new AbstractLongCollection () {

	  @Override
	  public boolean contains(final long v) {
	   return containsValue(v);
	  }

	  @Override
	  public it.unimi.dsi.fastutil.longs.LongIterator iterator() {
	   return new it.unimi.dsi.fastutil.longs.LongIterator () {
	    int pos = 0;
	    @Override
	    public boolean hasNext() {
	     return pos < size;
	    }

	    @Override

	    public long nextLong() {
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
	   Byte2LongArrayMap.this.clear();
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

	public Byte2LongArrayMap clone() {
	 Byte2LongArrayMap c;
	 try {
	  c = (Byte2LongArrayMap )super.clone();
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
	  s.writeByte(key[i]);
	  s.writeLong(value[i]);
	 }
	}

	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
	 s.defaultReadObject();
	 key = new byte[size];
	 value = new long[size];
	 for(int i = 0; i < size; i++) {
	  key[i] = s.readByte();
	  value[i] = s.readLong();
	 }
	}
}

