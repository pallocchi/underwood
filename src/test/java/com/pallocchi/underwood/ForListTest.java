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
	private static final Integer TIMEOUT = 100;
	
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
						Thread.sleep(TIMEOUT*10);
					} catch (InterruptedException e) {
						// Do nothing
					}
					return EXPECTED_VALUE;
				});
	}

}
