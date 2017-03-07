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
package com.pallocchi.underwood.commands;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class UnderwoodCommand<T> extends HystrixCommand<T>{
	
	private final Action<T> action;
	private final Fallback<T> fallback;
	
	private UnderwoodCommand(Setter setter, Action<T> action, Fallback<T> fallback) {
		super(setter);
		this.action = action;
		this.fallback = fallback;
	}
	
	@Override
	protected T run() throws Exception {
		return action.execute();
	}
	
	@Override
	protected T getFallback() {
		if (fallback != null) {
			return fallback.execute(getExecutionException());
		}
		return super.getFallback();
	}
	
	public static class Builder<T> {
		
		private String group;
		private String name;
		private Integer timeout;
		private Action<T> action;
		private Fallback<T> fallback;
		
		/**
		 * Sets the command group
		 * 
		 * @param group the command group
		 * @return the builder
		 */
		public Builder<T> withGroup(String group) {
			this.group = group;
			return this;
		}
		
		/**
		 * Sets the command name
		 * 
		 * @param name the command name
		 * @return the builder
		 */
		public Builder<T> withName(String name) {
			this.name = name;
			return this;
		}
		
		/**
		 * Sets the command timeout
		 * 
		 * @param timeout the command timeout
		 * @return the builder
		 */
		public Builder<T> withTimeout(int timeout) {
			this.timeout = timeout;
			return this;
		}
		
		/**
		 * Sets the command fall-back
		 * 
		 * @param fallback the command fall-back
		 * @return the builder
		 */
		public Builder<T> withFallback(Fallback<T> fallback) {
			this.fallback = fallback;
			return this;
		}
		
		/**
		 * Executes the command
		 * 
		 * @param action the action to execute
		 * @return the action result
		 */
		public T execute(Action<T> action) {
			this.action = action;
			return buildCommand().execute();
		}
		
		/**
		 * Creates the command with specified values
		 * 
		 * @return the command to execute
		 */
		protected UnderwoodCommand<T> buildCommand() {
			Setter setter = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(group)).andCommandKey(HystrixCommandKey.Factory.asKey(name));
			if (timeout != null) {
				setter.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeout));
			}
			return new UnderwoodCommand<T>(setter, action, fallback);
		}
		
	}
	
	public interface Action<T> {
		T execute();
	}
	
	public interface Fallback<T> {
		T execute(Throwable t);
	}

}