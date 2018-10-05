package it.unimi.dsi.fastutil.io;

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


import it.unimi.dsi.fastutil.bytes.ByteBigArrays;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class TextIOTest {

	static final byte[] SMALL = new byte[1024];
	static final byte[] LARGE = new byte[1024 * 1024 + 42];

	static {
		for(int i = SMALL.length; i-- != 0;) SMALL[i] = (byte)i;
		for(int i = LARGE.length; i-- != 0;) LARGE[i] = (byte)i;
	}

	public void testBytes(byte[] a) throws IOException {
		final File file = File.createTempFile(getClass().getSimpleName(), "dump");
		file.deleteOnExit();
		final byte[] aShifted = new byte[a.length + 1];
		System.arraycopy(a, 0, aShifted, 1, a.length);

		for(int i = 0; i < 4; i++) {
			file.delete();
			switch(i) {
			case 0: TextIO.storeBytes(a, file); break;
			case 1: TextIO.storeBytes(a, new PrintStream(file)); break;
			case 2: TextIO.storeBytes(aShifted, 1, a.length, file); break;
			case 3: TextIO.storeBytes(aShifted, 1, a.length, new PrintStream(file)); break;
			}
			byte[] b = new byte[a.length];
			assertEquals(a.length, TextIO.loadBytes(file, b));
			assertArrayEquals(a, b);

			TextIO.loadBytes(file, b, 0, a.length);
			assertArrayEquals(a, b);

			assertEquals(a.length, TextIO.loadBytes(new BufferedReader(new FileReader(file)), b));
			assertArrayEquals(a, b);
			assertEquals(a.length, TextIO.loadBytes(new BufferedReader(new FileReader(file)), b, 0, a.length));
			assertArrayEquals(a, b);

			byte[] c = new byte[a.length + 1];
			assertEquals(a.length, TextIO.loadBytes(new BufferedReader(new FileReader(file)), c));
			assertEquals(0, c[a.length]);
			System.arraycopy(c, 0, b, 0, b.length);
			assertArrayEquals(a, b);
			assertEquals(a.length, TextIO.loadBytes(new BufferedReader(new FileReader(file)), c, 1, a.length));
			assertEquals(0, c[0]);
			System.arraycopy(c, 1, b, 0, b.length);
			assertArrayEquals(a, b);
		}

	}

	@Test
	public void testBytes() throws IOException {
		testBytes(SMALL);
		testBytes(LARGE);
	}

	public void testBigBytes(byte[][] a) throws IOException {
		final File file = File.createTempFile(getClass().getSimpleName(), "dump");
		file.deleteOnExit();
		final long length = ByteBigArrays.length(a);
		final byte[][] aShifted = ByteBigArrays.newBigArray(length + 1);
		ByteBigArrays.copy(a, 0, aShifted, 1, length);

		for(int i = 0; i < 4; i++) {
			file.delete();
			switch(i) {
			case 0: TextIO.storeBytes(a, file); break;
			case 1: TextIO.storeBytes(a, new PrintStream(file)); break;
			case 2: TextIO.storeBytes(aShifted, 1, length, file); break;
			case 3: TextIO.storeBytes(aShifted, 1, length, new PrintStream(file)); break;
			}

			byte[][] b = ByteBigArrays.newBigArray(length);
			assertEquals(length, TextIO.loadBytes(file, b));
			assertArrayEquals(a, b);
			assertEquals(length, TextIO.loadBytes(file, b, 0, length));
			assertArrayEquals(a, b);

			assertEquals(length, TextIO.loadBytes(new BufferedReader(new FileReader(file)), b));
			assertArrayEquals(a, b);
			assertEquals(length, TextIO.loadBytes(new BufferedReader(new FileReader(file)), b, 0, length));
			assertArrayEquals(a, b);

			byte[][] c = ByteBigArrays.newBigArray(length + 1);
			assertEquals(length, TextIO.loadBytes(new BufferedReader(new FileReader(file)), c));
			assertEquals(0, ByteBigArrays.get(c, length));
			ByteBigArrays.copy(c, 0, b, 0, b.length);
			assertArrayEquals(a, b);
			assertEquals(length, TextIO.loadBytes(new BufferedReader(new FileReader(file)), c, 1, length));
			assertEquals(0, ByteBigArrays.get(c, 0));
			ByteBigArrays.copy(c, 1, b, 0, b.length);
			assertArrayEquals(a, b);
		}

	}

	@Test
	public void testBigBytes() throws IOException {
		testBigBytes(ByteBigArrays.wrap(SMALL));
		testBigBytes(ByteBigArrays.wrap(LARGE));
	}

	public void testFileDataWrappers() throws IOException {
		final File file = File.createTempFile(getClass().getSimpleName(), "dump");
		file.deleteOnExit();
		final DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
		for(int i = 0; i < 100; i++) dos.writeDouble(i);
		dos.close();

		DoubleIterator di = TextIO.asDoubleIterator(file);
		for(int i = 0; i < 100; i++) assertEquals(i, di.nextDouble(), 0.);
		assertFalse(di.hasNext());

		di = TextIO.asDoubleIterator(file);
		for(int i = 0; i < 100; i++) {
			assertTrue(di.hasNext());
			assertEquals(i, di.nextDouble(), 0.);
		}

		di = TextIO.asDoubleIterator(file);
		int s = 1;
		for(int i = 0; i < 100; i++) {
			assertEquals(Math.min(s, 100 - i), di.skip(s));
			i += s;
			if (i >= 100) break;
			assertEquals(i, di.nextDouble(), 0.);
			s *= 2;
		}

		di = TextIO.asDoubleIterator(file);
		s = 1;
		for(int i = 0; i < 100; i++) {
			if (s > 100 - i) break;
			assertTrue(di.hasNext());
			assertEquals(Math.min(s, 100 - i), di.skip(s));
			i += s;
			if (i >= 100) {
				assertFalse(di.hasNext());
				break;
			}
			assertTrue(di.hasNext());
			assertTrue(di.hasNext()); // To increase coverage
			assertEquals(i, di.nextDouble(), 0.);
			s *= 2;
		}
	}
}
