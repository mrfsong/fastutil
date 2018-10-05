package it.unimi.dsi.fastutil.ints;

/*
 * Copyright (C) 2017 Sebastiano Vigna
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

import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class Int2IntMapsTest {

	@SuppressWarnings("boxing")
	@Test
	public void testSingletonMapEqualsShouldCheckTheTypeOfParamters() {
		Int2IntMap map = Int2IntMaps.singleton(1, 2);
		assertFalse(map.equals(Collections.singletonMap(null, 2)));
		assertFalse(map.equals(Collections.singletonMap(1, null)));
		assertFalse(map.equals(Collections.singletonMap("foo", 2)));
		assertFalse(map.equals(Collections.singletonMap(1, "foo")));
	}

	@Test
	public void testToArrayShouldNullElementAfterLastEntry() {
		Int2IntMap map = Int2IntMaps.EMPTY_MAP;
		Object[] values = new Object[] { "test" };
		map.int2IntEntrySet().toArray(values);
		assertNull(values[0]);
	}

	@Test
	public void testReadingDefaultReturnValueFromUnmodifiableMap() {
		Int2IntMap map = Int2IntMaps.unmodifiable(Int2IntMaps.EMPTY_MAP);
		assert(map.defaultReturnValue() == 0);
	}

	@Test
	public void testFastIteratorHelpers() {
		Int2IntMap m = new Int2IntOpenHashMap();
		m.put(0, 0);
		m.put(1, 1);
		ObjectIterator<Entry> fastIterator = Int2IntMaps.fastIterator(m);
		Entry e = fastIterator.next();
		assertSame(e, fastIterator.next());

		ObjectIterable<Entry> fastIterable = Int2IntMaps.fastIterable(m);
		fastIterator = fastIterable.iterator();
		e = fastIterator.next();
		assertSame(e, fastIterator.next());

		Set<Entry> s = new HashSet<>();
		Int2IntMaps.fastIterable(m).forEach(s::add);
		assertEquals(1, s.size()); // Should be always the same entry, mutated
	}
}
