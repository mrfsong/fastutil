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

import org.junit.Test;

import java.util.RandomAccess;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntListsTest {
	@Test
	public void testRandomAccess() {
		final IntList fakeList = new AbstractIntList() {
			@Override
			public int getInt(int index) {
				return 0;
			}
			@Override
			public int size() {
				return 1;
			}
		};

		assertFalse(IntLists.unmodifiable(fakeList) instanceof RandomAccess);
		assertFalse(IntLists.synchronize(fakeList) instanceof RandomAccess);
		assertFalse(IntLists.synchronize(fakeList, new Object()) instanceof RandomAccess);

		assertTrue(IntLists.unmodifiable(new IntArrayList()) instanceof RandomAccess);
		assertTrue(IntLists.synchronize(new IntArrayList()) instanceof RandomAccess);
		assertTrue(IntLists.synchronize(new IntArrayList(), new Object()) instanceof RandomAccess);
	}
}
