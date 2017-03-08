/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2017 Pablo Pallocchi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package com.pallocchi.underwood;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.netflix.hystrix.exception.HystrixRuntimeException;

public class ForListTest {
	
	private static final String GROUP = "Test";
	private static final String NAME = ForListTest.class.getSimpleName();
	private static final Integer TIMEOUT = 1000;
	
	private static final List<Integer> EXPECTED_VALUE = Arrays.asList(1);
	private static final List<Integer> FALLBACK_VALUE = Collections.emptyList();
	
	@Test
	public void testSuccess() {
		List<Integer> result = Underwood.forList(Integer.class)
				.withGroup(GROUP)
				.withName(NAME)
				.execute(() -> EXPECTED_VALUE);
		Assert.assertArrayEquals(EXPECTED_VALUE.toArray(), result.toArray());
	}
	
	@Test
	public void testErrorWithFallback() {
		List<Integer> result = Underwood.forList(Integer.class)
				.withGroup(GROUP)
				.withName(NAME)
				.withFallback(e -> FALLBACK_VALUE)
				.execute(() -> {throw new IllegalArgumentException();});
		Assert.assertEquals(FALLBACK_VALUE, result);
	}
	
	@Test(expected = HystrixRuntimeException.class)
	public void testErrorWithoutFallback() {
		Underwood.forList(Integer.class)
				.withGroup(GROUP)
				.withName(NAME)
				.execute(() -> {throw new IllegalArgumentException();});
	}
	
	@Test(expected = HystrixRuntimeException.class)
	public void testWithTimeout() {
		Underwood.forList(Integer.class)
				.withGroup(GROUP)
				.withName(NAME)
				.withTimeout(TIMEOUT)
				.execute(() -> {
					try {
						// Simulate delay to throw a timeout
						Thread.sleep(TIMEOUT*2);
					} catch (InterruptedException e) {
						// Do nothing
					}
					return EXPECTED_VALUE;
				});
	}

}
