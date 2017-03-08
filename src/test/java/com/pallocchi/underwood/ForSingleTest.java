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

import org.junit.Assert;
import org.junit.Test;

import com.netflix.hystrix.exception.HystrixRuntimeException;

public class ForSingleTest {
	
	private static final String GROUP = "Test";
	private static final String NAME = ForSingleTest.class.getSimpleName();
	private static final Integer TIMEOUT = 1000;
	
	private static final Integer EXPECTED_VALUE = 1;
	private static final Integer FALLBACK_VALUE = 0;
	
	@Test
	public void testSuccess() {
		Integer result = Underwood.forSingle(Integer.class)
				.withGroup(GROUP)
				.withName(NAME)
				.execute(() -> EXPECTED_VALUE);
		Assert.assertEquals(EXPECTED_VALUE, result);
	}
	
	@Test
	public void testErrorWithFallback() {
		Integer result = Underwood.forSingle(Integer.class)
				.withGroup(GROUP)
				.withName(NAME)
				.withFallback(e -> FALLBACK_VALUE)
				.execute(() -> {throw new IllegalArgumentException();});
		Assert.assertEquals(FALLBACK_VALUE, result);
	}
	
	@Test(expected = HystrixRuntimeException.class)
	public void testErrorWithoutFallback() {
		Underwood.forSingle(Integer.class)
				.withGroup(GROUP)
				.withName(NAME)
				.execute(() -> {throw new IllegalArgumentException();});
	}
	
	@Test(expected = HystrixRuntimeException.class)
	public void testWithTimeout() {
		Underwood.forSingle(Integer.class)
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