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