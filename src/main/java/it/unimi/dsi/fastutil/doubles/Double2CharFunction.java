
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


package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;

/** A type-specific {@link Function}; provides some additional methods that use polymorphism to avoid (un)boxing.
	*
	* <p>Type-specific versions of {@code get()}, {@code put()} and
	* {@code remove()} cannot rely on {@code null} to denote absence of
	* a key. Rather, they return a {@linkplain #defaultReturnValue() default
	* return value}, which is set to 0/false at creation, but can be changed using
	* the {@code defaultReturnValue()} method.
	*
	* <p>For uniformity reasons, even functions returning objects implement the default
	* return value (of course, in this case the default return value is
	* initialized to {@code null}).
	*
	* <p>The default implementation of optional operations just throw an {@link
	* UnsupportedOperationException}, except for the type-specific {@code
	* containsKey()}, which return true. Generic versions of accessors delegate to
	* the corresponding type-specific counterparts following the interface rules.
	*
	* <p><strong>Warning:</strong> to fall in line as much as possible with the
	* {@linkplain java.util.Map standard map interface}, it is required that
	* standard versions of {@code get()}, {@code put()} and
	* {@code remove()} for maps with primitive-type keys or values <em>return
	* {@code null} to denote missing keys </em> rather than wrap the default
	* return value in an object. In case both keys and values are reference
	* types, the default return value must be returned instead, thus violating
	* the {@linkplain java.util.Map standard map interface} when the default
	* return value is not {@code null}.
	*
	* @see Function
	*/

@FunctionalInterface

public interface Double2CharFunction extends Function<Double, Character>, java.util.function.DoubleToIntFunction {
	/** {@inheritDoc}
	 * @since 8.0.0
	 */

	@Override
	default int applyAsInt(double operand) { return get(operand); }


	/** Adds a pair to the map (optional operation).
	 *
	 * @param key the key.
	 * @param value the value.
	 * @return the old value, or the {@linkplain #defaultReturnValue() default return value} if no value was present for the given key.
	 * @see Function#put(Object,Object)
	 */

	default char put(final double key, final char value) {
	 throw new UnsupportedOperationException();
	}

	/** Returns the value to which the given key is mapped.
	 *
	 * @param key the key.
	 * @return the corresponding value, or the {@linkplain #defaultReturnValue() default return value} if no value was present for the given key.
	 * @see Function#get(Object)
	 */

	char get(double key);

	/** Removes the mapping with the given key (optional operation).
	 * @param key the key.
	 * @return the old value, or the {@linkplain #defaultReturnValue() default return value} if no value was present for the given key.
	 * @see Function#remove(Object)
	 */

	default char remove(final double key) {
	 throw new UnsupportedOperationException();
	}



	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	default Character put(final Double key, final Character value) {
	 final double k = (key).doubleValue();
	 final boolean containsKey = containsKey(k);
	 final char v = put(k, (value).charValue());
	 return containsKey ? Character.valueOf(v) : null;
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	default Character get(final Object key) {

	 if (key == null) return null;

	 final double k = ((Double)(key)).doubleValue();
	 final char v = get(k);
	 return (v != defaultReturnValue() || containsKey(k)) ? Character.valueOf(v) : null;
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	default Character remove(final Object key) {

	 if (key == null) return null;

	 final double k = ((Double)(key)).doubleValue();
	 return containsKey(k) ? Character.valueOf(remove(k)) : null;
	}



	/** Returns true if this function contains a mapping for the specified key.
	 *
	 * <p>Note that for some kind of functions (e.g., hashes) this method will
	 * always return true. In particular, this default implementation always
	 * returns true.
	 *
	 * @param key the key.
	 * @return true if this function associates a value to {@code key}.
	 * @see Function#containsKey(Object)
	 */
	default boolean containsKey(double key) {
	 return true;
	}

	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */

	@Deprecated
	@Override
	default boolean containsKey(final Object key) {
	 return key == null ? false : containsKey(((Double)(key)).doubleValue());
	}





	/** Sets the default return value (optional operation).
	 *
	 * This value must be returned by type-specific versions of
	 * {@code get()}, {@code put()} and {@code remove()} to
	 * denote that the map does not contain the specified key. It must be
	 * 0/{@code false}/{@code null} by default.
	 *
	 * @param rv the new default return value.
	 * @see #defaultReturnValue()
	 */

	default void defaultReturnValue(char rv) {
	 throw new UnsupportedOperationException();
	}


	/** Gets the default return value.
	 *
	 * <p>This default implementation just return the default null value
	 * of the type ({@code null} for objects, 0 for scalars, false for Booleans).
	 *
	 * @return the current default return value.
	 */

	default char defaultReturnValue() {
	 return ((char)0);
	}
}

