package com.pallocchi.underwood;

import org.junit.Assert;
import org.junit.Test;

import com.netflix.hystrix.exception.HystrixRuntimeException;

public class ForSingleTest {
	
	private static final String GROUP = "Test";
	private static final String NAME = ForSingleTest.class.getSimpleName();
	private static final Integer TIMEOUT = 100;
	
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
						Thread.sleep(TIMEOUT*10);
					} catch (InterruptedException e) {
						// Do nothing
					}
					return EXPECTED_VALUE;
				});
	}

}