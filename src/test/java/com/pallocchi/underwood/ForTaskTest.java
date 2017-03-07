package com.pallocchi.underwood;

import org.junit.Test;

import com.netflix.hystrix.exception.HystrixRuntimeException;

public class ForTaskTest {
	
	private static final String GROUP = "Test";
	private static final String NAME = ForTaskTest.class.getSimpleName();
	private static final Integer TIMEOUT = 100;
	
	@Test(expected = HystrixRuntimeException.class)
	public void testErrorWithoutFallback() {
		Underwood.forTask()
				.withGroup(GROUP)
				.withName(NAME)
				.execute(() -> {throw new IllegalArgumentException();});
	}
	
	@Test(expected = HystrixRuntimeException.class)
	public void testWithTimeout() {
		Underwood.forTask()
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
				});
	}

}