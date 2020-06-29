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

import java.util.ArrayList;

/**
 * Represents a database connection. 
 * 
 * @author uwe geercken - uwe.geercken@web.de
 *
 */
public class HopDatabaseConnection 
{
	private String name;
	private String pluginId;
	private String odbcDsn;
	private String databaseName;
	private int accessType;
	private String hostname;
	private String user;
	private String password;
	private String port;
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

	public String getPort() 
	{
		return port;
	}

	public void setPort(String port) 
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

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getUser() 
	{
		return user;
	}

	public void setUser(String user) 
	{
		this.user = user;
	}
}
