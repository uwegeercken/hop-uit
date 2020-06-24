/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.datamelt.hop.utils;

import java.util.HashMap;
import java.util.Map;

public class SystemVariables 
{
	private static Map<String, String> variables;
	
	/**
	 * Reads all system variables into a map
	 * 
	 */
	static
	{
		variables = System.getenv();
	}
	
	public static boolean existVariable(String key)
	{
		return variables.containsKey(key);
	}
	
	/**
	 * Returns all system variables
	 * 
	 * @return	all system variables
	 */
	public static Map<String, String> getVariables()
	{
		return variables;
	}
	
	/**
	 * Filters system variables by a given prefix.
	 * 
	 * @param prefix			prefix the system variables have to start with
	 * @return					map of key/values for the given prefix
	 */
	public static Map<String, String> getVariables(String prefix)
	{
		return getVariables(prefix, false, false);
	}
	
	/**
	 * Filters system variables by a given prefix. 
	 * 
	 * The prefix is removed from the key if removePrefix is set to true, but only
	 * if the key is longer than the prefix itself.
	 *  
	 * Keys are converted to uppercase if keysToUpperCase is set to true. 
	 * 
	 * @param prefix			prefix the system variables have to start with
	 * @param removePrefix		remove the prefix from the key
	 * @param keysToUpperCase	convert keys to uppercase
	 * @return					map of key/values for the given prefix
	 */
	public static Map<String, String> getVariables(String prefix, boolean removePrefix, boolean keysToUpperCase)
	{
		Map<String, String> filteredVariables = new HashMap<String, String>();
		for (String key : variables.keySet())
		{
			// always compare case insensitive
			if(key.toLowerCase().startsWith(prefix.toLowerCase()))
			{
				String currentKey = key;
				// convert to uppercase
				if(keysToUpperCase)
				{
					currentKey = currentKey.toUpperCase();
				}
				// remove the prefix from the key
				// only for keys which are longer than the preifx itself
				if(removePrefix && currentKey.length()>prefix.length())
				{
					currentKey = currentKey.substring(prefix.length());
				}
				
				filteredVariables.put(currentKey, variables.get(key));
			}
        }

		return filteredVariables;
	}
}
