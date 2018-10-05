package it.unimi.dsi.fastutil.doubles;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DoubleOpenHashSetTest {

	@Test
	public void testNaNs() {
		DoubleOpenHashSet s = new DoubleOpenHashSet();
		s.add(Double.NaN);
		s.add(Double.NaN);
		assertEquals(1, s.size());
	}

	@Test
	public void testZeros() {
		DoubleOpenHashSet s = new DoubleOpenHashSet();
		assertTrue(s.add(-0.0d));
		assertTrue(s.add(+0.0d));
		assertEquals(2, s.size());
	}

}
