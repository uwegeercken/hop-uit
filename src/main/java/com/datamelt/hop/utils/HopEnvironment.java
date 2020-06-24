package com.datamelt.hop.utils;

import java.io.File;
import java.util.ArrayList;

public class HopEnvironment 
{
	private String name;
	private String homeFolder;
	private String filesFolder;
	
	private String description;
	private String version;
	private String company;
	private String department;
	private String project;
	private String metadataBaseFolder;
	private String unitTestsBasePath;
	private String dataSetsCsvFolder;
	private String enforcingExecutionInHome;
	private ArrayList<Variable> variables = new ArrayList<>();
	
	public HopEnvironment(String name, String description, String homeFolder)
	{
		this.name = name;
		this.description = description;
		this.homeFolder = homeFolder;
		this.filesFolder = homeFolder + File.separator + Constants.FOLDER_FILES;
	}
	
	public HopEnvironment(String name, String homeFolder)
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
	
	public String getDescription() 
	{
		return description;
	}
	
	public void setDescription(String description) 
	{
		this.description = description;
	}
	
	public String getVersion() 
	{
		return version;
	}
	
	public void setVersion(String version) 
	{
		this.version = version;
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
	
	public String getProject() 
	{
		return project;
	}
	
	public void setProject(String project) 
	{
		this.project = project;
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

	public String getFilesFolder() 
	{
		return filesFolder;
	}
	
	
}
