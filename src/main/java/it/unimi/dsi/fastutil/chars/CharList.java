
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


package it.unimi.dsi.fastutil.chars;

import java.util.List;



/** A type-specific {@link List}; provides some additional methods that use polymorphism to avoid (un)boxing.
	*
	* <p>Note that this type-specific interface extends {@link Comparable}: it is expected that implementing
	* classes perform a lexicographical comparison using the standard operator "less then" for primitive types,
	* and the usual {@link Comparable#compareTo(Object) compareTo()} method for objects.
	*
	* <p>Additionally, this interface strengthens {@link #listIterator()},
	* {@link #listIterator(int)} and {@link #subList(int,int)}.
	*
	* <p>Besides polymorphic methods, this interfaces specifies methods to copy into an array or remove contiguous
	* sublists. Although the abstract implementation of this interface provides simple, one-by-one implementations
	* of these methods, it is expected that concrete implementation override them with optimized versions.
	*
	* @see List
	*/

public interface CharList extends List<Character>, Comparable<List<? extends Character>>, CharCollection {
	/** Returns a type-specific iterator on the elements of this list.
	 *
	 * <p>Note that this specification strengthens the one given in {@link List#iterator()}.
	 * It would not be normally necessary, but {@link java.lang.Iterable#iterator()} is bizarrily re-specified
	 * in {@link List}.
	 *
	 * @return an iterator on the elements of this list.
	 */
	@Override
	CharListIterator iterator();

	/** Returns a type-specific list iterator on the list.
	 *
	 * @see List#listIterator()
	 */
	@Override
	CharListIterator listIterator();

	/** Returns a type-specific list iterator on the list starting at a given index.
	 *
	 * @see List#listIterator(int)
	 */
	@Override
	CharListIterator listIterator(int index);

	/** Returns a type-specific view of the portion of this list from the index {@code from}, inclusive, to the index {@code to}, exclusive.
	 *
	 * <p>Note that this specification strengthens the one given in {@link List#subList(int,int)}.
	 *
	 * @see List#subList(int,int)
	 */
	@Override
	CharList subList(int from, int to);

	/** Sets the size of this list.
	 *
	 * <p>If the specified size is smaller than the current size, the last elements are
	 * discarded. Otherwise, they are filled with 0/{@code null}/{@code false}.
	 *
	 * @param size the new size.
	 */

	void size(int size);

	/** Copies (hopefully quickly) elements of this type-specific list into the given array.
	 *
	 * @param from the start index (inclusive).
	 * @param a the destination array.
	 * @param offset the offset into the destination array where to store the first element copied.
	 * @param length the number of elements to be copied.
	 */
	void getElements(int from, char a[], int offset, int length);

	/** Removes (hopefully quickly) elements of this type-specific list.
	 *
	 * @param from the start index (inclusive).
	 * @param to the end index (exclusive).
	 */
	void removeElements(int from, int to);

	/** Add (hopefully quickly) elements to this type-specific list.
	 *
	 * @param index the index at which to add elements.
	 * @param a the array containing the elements.
	 */
	void addElements(int index, char a[]);

	/** Add (hopefully quickly) elements to this type-specific list.
	 *
	 * @param index the index at which to add elements.
	 * @param a the array containing the elements.
	 * @param offset the offset of the first element to add.
	 * @param length the number of elements to add.
	 */
	void addElements(int index, char a[], int offset, int length);



	/** Appends the specified element to the end of this list (optional operation).
	 * @see List#add(Object)
	 */
	@Override
	boolean add(char key);

	/** Inserts the specified element at the specified position in this list (optional operation).
	 * @see List#add(int,Object)
	 */
	void add(int index, char key);

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	default void add(int index, Character key) {
	 add(index, (key).charValue());
	}

	/** Inserts all of the elements in the specified type-specific collection into this type-specific list at the specified position (optional operation).
	 * @see List#addAll(int,java.util.Collection)
	 */
	boolean addAll(int index, CharCollection c);

	/** Inserts all of the elements in the specified type-specific list into this type-specific list at the specified position (optional operation).
	 * @see List#add(int,Object)
	 */
	boolean addAll(int index, CharList c);

	/** Appends all of the elements in the specified type-specific list to the end of this type-specific list (optional operation).
	 * @see List#add(int,Object)
	 */
	boolean addAll(CharList c);

	/** Replaces the element at the specified position in this list with the specified element (optional operation).
	 * @see List#set(int,Object)
	 */
	char set(int index, char k);

	/** Returns the element at the specified position in this list.
	 * @see List#get(int)
	 */
	char getChar(int index);

	/** Returns the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element.
	 * @see List#indexOf(Object)
	 */
	int indexOf(char k);

	/** Returns the index of the last occurrence of the specified element in this list, or -1 if this list does not contain the element.
	 * @see List#lastIndexOf(Object)
	 */
	int lastIndexOf(char k);

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	default boolean contains(final Object key) {
	 return CharCollection.super.contains(key);
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	default Character get(int index) {
	 return Character.valueOf(getChar(index));
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	default int indexOf(Object o) {
	 return indexOf(((Character)(o)).charValue());
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	default int lastIndexOf(Object o) {
	 return lastIndexOf(((Character)(o)).charValue());
	}

	/** {@inheritDoc}
	 * <p>This method specification is a workaround for
	 * <a href="http://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8177440">bug 8177440</a>.
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	default boolean add(Character k) {
	 return add((k).charValue());
	}

	/** Removes the element at the specified position in this list (optional operation).
	 * @see List#remove(int)
	 */
	char removeChar(int index);

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead.
	 */
	@Deprecated
	@Override
	default boolean remove(final Object key) {
	 return CharCollection.super.remove(key);
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	default Character remove(int index) {
	 return Character.valueOf(removeChar(index));
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	default Character set(int index, Character k) {
	 return Character.valueOf(set(index, (k).charValue()));
	}



}

