package com.datamelt.hop.utils;

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
