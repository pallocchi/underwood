package com.pallocchi.underwood.commands;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class UnderwoodVoidCommand extends HystrixCommand<Void>{
	
	private final Action action;
	private final Fallback fallback;
	
	private UnderwoodVoidCommand(Setter setter, Action action, Fallback fallback) {
		super(setter);
		this.action = action;
		this.fallback = fallback;
	}
	
	@Override
	protected Void run() throws Exception {
		action.execute();
		return null;
	}
	
	@Override
	protected Void getFallback() {
		if (fallback != null) {
			fallback.execute(getExecutionException());
		}
		return super.getFallback();
	}
	
	public static class Builder {
		
		private String group;
		private String name;
		private Integer timeout;
		private Action action;
		private Fallback fallback;
		
		/**
		 * Sets the command group
		 * 
		 * @param group the command group
		 * @return the builder
		 */
		public Builder withGroup(String group) {
			this.group = group;
			return this;
		}
		
		/**
		 * Sets the command name
		 * 
		 * @param name the command name
		 * @return the builder
		 */
		public Builder withName(String name) {
			this.name = name;
			return this;
		}
		
		/**
		 * Sets the command timeout
		 * 
		 * @param timeout the command timeout
		 * @return the builder
		 */
		public Builder withTimeout(int timeout) {
			this.timeout = timeout;
			return this;
		}
		
		/**
		 * Sets the command fall-back
		 * 
		 * @param fallback the command fall-back
		 * @return the builder
		 */
		public Builder withFallback(Fallback fallback) {
			this.fallback = fallback;
			return this;
		}
		
		/**
		 * Executes the command
		 * 
		 * @param action the action to execute
		 * @return the action result
		 */
		public void execute(Action action) {
			this.action = action;
			buildCommand().execute();
		}
		
		/**
		 * Creates the command with specified values
		 * 
		 * @return the command to execute
		 */
		protected UnderwoodVoidCommand buildCommand() {
			Setter setter = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(group)).andCommandKey(HystrixCommandKey.Factory.asKey(name));
			if (timeout != null) {
				setter.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeout));
			}
			return new UnderwoodVoidCommand(setter, action, fallback);
		}
		
	}
	
	public interface Action {
		void execute();
	}
	
	public interface Fallback {
		void execute(Throwable t);
	}

}