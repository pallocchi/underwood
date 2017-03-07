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