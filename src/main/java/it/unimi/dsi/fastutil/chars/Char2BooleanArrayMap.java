
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


package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
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

public class Char2BooleanArrayMap extends AbstractChar2BooleanMap implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	/** The keys (valid up to {@link #size}, excluded). */
	private transient char[] key;
	/** The values (parallel to {@link #key}). */
	private transient boolean[] value;
	/** The number of valid entries in {@link #key} and {@link #value}. */
	private int size;

	/** Creates a new empty array map with given key and value backing arrays. The resulting map will have as many entries as the given arrays.
	 *
	 * <p>It is responsibility of the caller that the elements of {@code key} are distinct.
	 *
	 * @param key the key array.
	 * @param value the value array (it <em>must</em> have the same length as {@code key}).
	 */
	public Char2BooleanArrayMap(final char[] key, final boolean[] value) {
	 this.key = key;
	 this.value = value;
	 size = key.length;
	 if(key.length != value.length) throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
	}

	/** Creates a new empty array map.
	 */
	public Char2BooleanArrayMap() {
	 this.key = CharArrays.EMPTY_ARRAY;
	 this.value = BooleanArrays.EMPTY_ARRAY;
	}

	/** Creates a new empty array map of given capacity.
	 *
	 * @param capacity the initial capacity.
	 */
	public Char2BooleanArrayMap(final int capacity) {
	 this.key = new char[capacity];
	 this.value = new boolean[capacity];
	}

	/** Creates a new empty array map copying the entries of a given map.
	 *
	 * @param m a map.
	 */
	public Char2BooleanArrayMap(final Char2BooleanMap m) {
	 this(m.size());
	 putAll(m);
	}

	/** Creates a new empty array map copying the entries of a given map.
	 *
	 * @param m a map.
	 */
	public Char2BooleanArrayMap(final Map<? extends Character, ? extends Boolean> m) {
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
	public Char2BooleanArrayMap(final char[] key, final boolean[] value, final int size) {
	 this.key = key;
	 this.value = value;
	 this.size = size;
	 if(key.length != value.length) throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
	 if (size > key.length) throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the backing-arrays size (" + key.length + ")");
	}

	private final class EntrySet extends AbstractObjectSet<Char2BooleanMap.Entry > implements FastEntrySet {

	 @Override
	 public ObjectIterator<Char2BooleanMap.Entry > iterator() {
	  return new ObjectIterator<Char2BooleanMap.Entry >() {
	   int curr = -1, next = 0;

	   @Override
	   public boolean hasNext() { return next < size; }

	   @Override

	   public Entry next() {
	    if (! hasNext()) throw new NoSuchElementException();
	    return new AbstractChar2BooleanMap.BasicEntry ( key[curr = next], value[next++]);
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
	 public ObjectIterator<Char2BooleanMap.Entry > fastIterator() {
	  return new ObjectIterator<Char2BooleanMap.Entry >() {
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

	  if (e.getKey() == null || ! (e.getKey() instanceof Character)) return false;


	  if (e.getValue() == null || ! (e.getValue() instanceof Boolean)) return false;

	  final char k = ((Character)(e.getKey())).charValue();
	  return Char2BooleanArrayMap.this.containsKey(k) && ( (Char2BooleanArrayMap.this.get(k)) == (((Boolean)(e.getValue())).booleanValue()) );
	 }

	 @Override

	 public boolean remove(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;
	  final Map.Entry<?,?> e = (Map.Entry<?,?>)o;

	  if (e.getKey() == null || ! (e.getKey() instanceof Character)) return false;


	  if (e.getValue() == null || ! (e.getValue() instanceof Boolean)) return false;

	  final char k = ((Character)(e.getKey())).charValue();
	  final boolean v = ((Boolean)(e.getValue())).booleanValue();

	  final int oldPos = Char2BooleanArrayMap.this.findKey(k);
	  if (oldPos == -1 || ! ( (v) == (Char2BooleanArrayMap.this.value[oldPos]) )) return false;
	  final int tail = size - oldPos - 1;
	  System.arraycopy(Char2BooleanArrayMap.this.key, oldPos + 1, Char2BooleanArrayMap.this.key, oldPos, tail);
	  System.arraycopy(Char2BooleanArrayMap.this.value, oldPos + 1, Char2BooleanArrayMap.this.value, oldPos, tail);
	  Char2BooleanArrayMap.this.size--;






	  return true;
	 }
	}

	@Override
	public FastEntrySet char2BooleanEntrySet() { return new EntrySet(); }

	private int findKey(final char k) {
	 final char[] key = this.key;
	 for(int i = size; i-- != 0;) if (( (key[i]) == (k) )) return i;
	 return -1;
	}

	@Override

	public boolean get(final char k) {
	 final char[] key = this.key;
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
	public boolean containsKey(final char k) { return findKey(k) != -1; }

	@Override
	public boolean containsValue(boolean v) {
	 for(int i = size; i-- != 0;) if (( (value[i]) == (v) )) return true;
	 return false;
	}

	@Override
	public boolean isEmpty() { return size == 0; }

	@Override

	public boolean put(char k, boolean v) {
	 final int oldKey = findKey(k);
	 if (oldKey != -1) {
	  final boolean oldValue = value[oldKey];
	  value[oldKey] = v;
	  return oldValue;
	 }
	 if (size == key.length) {
	  final char[] newKey = new char[size == 0 ? 2 : size * 2];
	  final boolean[] newValue = new boolean[size == 0 ? 2 : size * 2];
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

	public boolean remove(final char k) {
	 final int oldPos = findKey(k);
	 if (oldPos == -1) return defRetValue;
	 final boolean oldValue = value[oldPos];
	 final int tail = size - oldPos - 1;
	 System.arraycopy(key, oldPos + 1, key, oldPos, tail);
	 System.arraycopy(value, oldPos + 1, value, oldPos, tail);
	 size--;






	 return oldValue;
	}

	@Override
	public CharSet keySet() {
	 return new AbstractCharSet () {
	  @Override
	  public boolean contains(final char k) {
	   return findKey(k) != -1;
	  }

	  @Override
	  public boolean remove(final char k) {
	    final int oldPos = findKey(k);
	    if (oldPos == -1) return false;
	    final int tail = size - oldPos - 1;
	    System.arraycopy(key, oldPos + 1, key, oldPos, tail);
	    System.arraycopy(value, oldPos + 1, value, oldPos, tail);
	    size--;
	    return true;
	  }

	  @Override
	  public CharIterator iterator() {
	   return new CharIterator () {
	    int pos = 0;
	    @Override
	    public boolean hasNext() {
	     return pos < size;
	    }

	    @Override

	    public char nextChar() {
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
	   Char2BooleanArrayMap.this.clear();
	  }
	 };
	}

	@Override
	public BooleanCollection values() {
	 return new AbstractBooleanCollection () {

	  @Override
	  public boolean contains(final boolean v) {
	   return containsValue(v);
	  }

	  @Override
	  public it.unimi.dsi.fastutil.booleans.BooleanIterator iterator() {
	   return new it.unimi.dsi.fastutil.booleans.BooleanIterator () {
	    int pos = 0;
	    @Override
	    public boolean hasNext() {
	     return pos < size;
	    }

	    @Override

	    public boolean nextBoolean() {
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
	   Char2BooleanArrayMap.this.clear();
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

	public Char2BooleanArrayMap clone() {
	 Char2BooleanArrayMap c;
	 try {
	  c = (Char2BooleanArrayMap )super.clone();
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
	  s.writeChar(key[i]);
	  s.writeBoolean(value[i]);
	 }
	}

	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
	 s.defaultReadObject();
	 key = new char[size];
	 value = new boolean[size];
	 for(int i = 0; i < size; i++) {
	  key[i] = s.readChar();
	  value[i] = s.readBoolean();
	 }
	}
}

