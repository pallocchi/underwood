package com.pallocchi.underwood;

import java.util.List;
import java.util.Set;

import com.pallocchi.underwood.commands.UnderwoodCommand;
import com.pallocchi.underwood.commands.UnderwoodVoidCommand;

public class Underwood {

	/**
	 * Creates a builder for a command that performs a task without any results
	 * 
	 * @return the command builder
	 */
	public static UnderwoodVoidCommand.Builder forTask() {
		return new UnderwoodVoidCommand.Builder();
	}
	
	/**
	 * Creates a builder for a command that retrieves a single result of specified type
	 * 
	 * @param type the result type
	 * @return the command builder
	 */
	public static <T> UnderwoodCommand.Builder<T> forSingle(Class<T> type) {
		return new UnderwoodCommand.Builder<T>();
	}
	
	/**
	 * Creates a builder for a command that retrieves a list of results of specified type
	 * 
	 * @param type the result type
	 * @return the command builder
	 */
	public static <T> UnderwoodCommand.Builder<List<T>> forList(Class<T> type) {
		return new UnderwoodCommand.Builder<List<T>>();
	}
	
	/**
	 * Create a builder for a command that retrieves a set of results of specified type
	 * 
	 * @param type the result type
	 * @return the command builder
	 */
	public static <T> UnderwoodCommand.Builder<Set<T>> forSet(Class<T> type) {
		return new UnderwoodCommand.Builder<Set<T>>();
	}
	
}