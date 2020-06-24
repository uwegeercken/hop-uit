package com.datamelt.hop.utils;

import java.util.ArrayList;

public class HopDatabaseConnection 
{
	private String name;
	private String pluginId;
	private String odbcDsn;
	private String databaseName;
	private int accessType;
	private String hostname;
	private String password;
	private int port;
	private String pluginName;
	private String driverClassName;
	
	private ArrayList<Variable> attributes = new ArrayList<>();
	
	public HopDatabaseConnection(String pluginId)
	{
		this.pluginId = pluginId;
	}
	
	public void addAttribute(String name, Object value)
	{
		attributes.add(new Variable(name,value));
	}
	
	public Variable getAttribute(int index)
	{
		return attributes.get(index);
	}
	
	public Variable getAttribute(String name)
	{
		Variable foundVariable = null;
		for(Variable var : attributes)
		{
			if(var.getName().equals(name))
			{
				foundVariable = var;
				break;
			}
		}
		return foundVariable;
	}

	public String getOdbcDsn() 
	{
		return odbcDsn;
	}

	public void setOdbcDsn(String odbcDsn) 
	{
		this.odbcDsn = odbcDsn;
	}

	public String getDatabaseName() 
	{
		return databaseName;
	}

	public void setDatabaseName(String databaseName) 
	{
		this.databaseName = databaseName;
	}

	public int getAccessType() 
	{
		return accessType;
	}

	public void setAccessType(int accessType) 
	{
		this.accessType = accessType;
	}

	public String getHostname() 
	{
		return hostname;
	}

	public void setHostname(String hostname) 
	{
		this.hostname = hostname;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public int getPort() 
	{
		return port;
	}

	public void setPort(int port) 
	{
		this.port = port;
	}

	public String getPluginName() 
	{
		return pluginName;
	}

	public void setPluginName(String pluginName) 
	{
		this.pluginName = pluginName;
	}

	public String getDriverClassName() 
	{
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) 
	{
		this.driverClassName = driverClassName;
	}

	public ArrayList<Variable> getAttributes() 
	{
		return attributes;
	}

	public void setAttributes(ArrayList<Variable> attributes) 
	{
		this.attributes = attributes;
	}

	public String getPluginId() 
	{
		return pluginId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
