package com.pallocchi.underwood;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.netflix.hystrix.exception.HystrixRuntimeException;

public class ForSetTest {
	
	private static final String GROUP = "Test";
	private static final String NAME = ForSetTest.class.getSimpleName();
	private static final Integer TIMEOUT = 100;
	
	private static final Set<Integer> EXPECTED_VALUE = new HashSet<>(Arrays.asList(1));
	private static final Set<Integer> FALLBACK_VALUE = Collections.emptySet();
	
	@Test
	public void testSuccess() {
		Set<Integer> result = Underwood.forSet(Integer.class)
				.withGroup(GROUP)
				.withName(NAME)
				.execute(() -> EXPECTED_VALUE);
		Assert.assertArrayEquals(EXPECTED_VALUE.toArray(), result.toArray());
	}
	
	@Test
	public void testErrorWithFallback() {
		Set<Integer> result = Underwood.forSet(Integer.class)
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
		Underwood.forSet(Integer.class)
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