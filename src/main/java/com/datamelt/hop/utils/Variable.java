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

/**
 * Helper class defining a variable with a key, value and description.
 * 
 * @author uwe geercken - uwe.geercken@web.de
 *
 */
public class Variable 
{
	private String name;
	private String description;
	private Object value;
	
	public Variable(String name, String description, Object value)
	{
		this.name = name;
		this.description = description;
		this.value = value;
	}
	
	public Variable(String name, Object value)
	{
		this.name = name;
		this.value = value;
	}

	public String getName() 
	{
		return name;
	}

	public String getDescription() 
	{
		return description;
	}

	public String getStringValue() 
	{
		return value.toString();
	}
	
	public int getIntValue() 
	{
		return (int) value;
	}
	
	public long getLongValue() 
	{
		return (long) value;
	}
	
	public float getFloatValue() 
	{
		return (float) value;
	}
	
	public double getDoubleValue() 
	{
		return (double) value;
	}
	
	public boolean getBooleanValue() 
	{
		return (boolean) value;
	}
}
