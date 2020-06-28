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

import java.io.File;
import java.util.ArrayList;

/**
 * Represnts a Hop project.
 * 
 * 
 * @author uwe
 *
 */
public class HopProject
{
	private String name;
	private String homeFolder;
	
	private String description;
	private String company;
	private String department;
	private String metadataBaseFolder;
	private String unitTestsBasePath;
	private String dataSetsCsvFolder;
	private String enforcingExecutionInHome;
	private ArrayList<Variable> variables = new ArrayList<>();
	
	private ArrayList<TranslationFile> translationFiles = new ArrayList<TranslationFile>();
	
	public HopProject(String name, String description, String homeFolder)
	{
		this.name = name;
		this.description = description;
		this.homeFolder = homeFolder;
	}
	
	public HopProject(String name, String homeFolder)
	{
		this.name = name;
		this.homeFolder = homeFolder;
	}
	
	public void addVariable(String name, String description, Object value)
	{
		variables.add(new Variable(name,description,value));
	}
	
	public Variable getVariable(int index)
	{
		return variables.get(index);
	}
	
	public Variable getVariable(String name)
	{
		Variable foundVariable = null;
		for(Variable var : variables)
		{
			if(var.getName().equals(name))
			{
				foundVariable = var;
				break;
			}
		}
		return foundVariable;
	}
	
	public void addTranslationFile(TranslationFile translationFile)
	{
		translationFiles.add(translationFile);
	}
	
	public int getNumberOfTranslationFiles()
	{
		return translationFiles.size();
	}
	
	public String getDescription() 
	{
		return description;
	}
	
	public void setDescription(String description) 
	{
		this.description = description;
	}
	
	public String getCompany() 
	{
		return company;
	}
	
	public void setCompany(String company) 
	{
		this.company = company;
	}
	
	public String getDepartment() 
	{
		return department;
	}
	
	public void setDepartment(String department) 
	{
		this.department = department;
	}
	
	public String getMetadataBaseFolder() 
	{
		return metadataBaseFolder;
	}
	
	public void setMetadataBaseFolder(String metadataBaseFolder) 
	{
		this.metadataBaseFolder = metadataBaseFolder;
	}
	
	public String getUnitTestsBasePath() 
	{
		return unitTestsBasePath;
	}
	
	public void setUnitTestsBasePath(String unitTestsBasePath) 
	{
		this.unitTestsBasePath = unitTestsBasePath;
	}
	
	public String getDataSetsCsvFolder() 
	{
		return dataSetsCsvFolder;
	}
	
	public void setDataSetsCsvFolder(String dataSetsCsvFolder) 
	{
		this.dataSetsCsvFolder = dataSetsCsvFolder;
	}
	
	public String getEnforcingExecutionInHome() 
	{
		return enforcingExecutionInHome;
	}
	
	public void setEnforcingExecutionInHome(String enforcingExecutionInHome) 
	{
		this.enforcingExecutionInHome = enforcingExecutionInHome;
	}
	
	public ArrayList<Variable> getVariables() 
	{
		return variables;
	}
	
	public void setVariables(ArrayList<Variable> variables) 
	{
		this.variables = variables;
	}

	public String getName() 
	{
		return name;
	}

	public String getHomeFolder() 
	{
		return homeFolder;
	}

	public ArrayList<TranslationFile> getTranslationFiles() 
	{
		return translationFiles;
	}
	
	public String getDatabaseConnectionsFolder()
	{
		return name + File.separator + Constants.PROJECT_METADATA_FOLDER_NAME + File.separator + Constants.PROJECT_DATABASE_FOLDER_NAME;
	}

}
