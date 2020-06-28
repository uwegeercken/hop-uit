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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * Collects and uses all projects that are created during the conversion process.
 * 
 * @author uwe geercken - uwe.geercken@web.de
 *
 */
public class HopProjectCollection
{
	private HashMap<String, HopProject> projects = new HashMap<>();
	private ArrayList<TranslationFile> translationFiles = new ArrayList<>(); 
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.PROJECT_VERSION_DATE_FORMAT);
	
	private String inputFolder;
	private String outputFolder;
	private boolean projectPerSubfolder;
	
	public HopProjectCollection(String inputFolder, String outputFolder, boolean processAllFiles, boolean projectPerSubfolder) throws Exception
	{
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
		this.projectPerSubfolder = projectPerSubfolder;
		
		// always add a default project
		addDefaultProject();
		
		// create a project per subfolder of the input folder
		if(projectPerSubfolder)
		{
			addProjects(FileUtils.getSubfolders(inputFolder));
		}
		
		// traverse the filesystem for all files
		if(processAllFiles)
		{
			FileUtils.traverseFilesystem(inputFolder, new File(inputFolder), translationFiles);
		}
		
		addTranslationFilesToProjects();
	}
	
	public void addTranslationFiles(ArrayList<TranslationFile>translationFiles)
	{
		translationFiles.addAll(translationFiles);
		addTranslationFilesToProjects();
	}
	
	private void addTranslationFilesToProjects()
	{
		// loop over all files and add them to the relevant projects
		for(TranslationFile tFile: translationFiles)
		{
			addFileToProject(tFile);
		}
	}
	
	public void createProjectFolders() throws Exception
	{
		for(String projectName : projects.keySet())
		{
			HopProject project = projects.get(projectName);
			if(project.getNumberOfTranslationFiles()>0)
			{
				FileUtils.createFolder(project.getHomeFolder());
				
				String metadataFolder = project.getHomeFolder() + File.separator + Constants.PROJECT_METADATA_FOLDER_NAME;
				
				FileUtils.createFolder(metadataFolder);
				FileUtils.createFolder(metadataFolder + File.separator + Constants.PROJECT_DATABASE_FOLDER_NAME);
				//FileUtils.createFolder(metadataFolder + File.separator + Constants.PROJECT_PIPELINE_RUNCONFIG_FOLDER_NAME);
				//FileUtils.createFolder(metadataFolder + File.separator + Constants.PROJECT_WORKFLOW_RUNCONFIG_FOLDER_NAME);
			}
		}

	}
	
	public void addDefaultProject()
	{
		addProject(Constants.DEFAULT_PROJECT_NAME, Constants.DEFAULT_PROJECT_DESCRIPTION_PARTIAL + " " + dateFormat.format(new Date()), outputFolder + File.separator + Constants.DEFAULT_PROJECT_NAME);
	}
	
	public void addProjects(ArrayList<String> subfolders)
	{
		for(String subfolder : subfolders)
		{
			addProject(subfolder, Constants.DEFAULT_PROJECT_DESCRIPTION_PARTIAL + dateFormat.format(new Date()), outputFolder + File.separator + subfolder);
		}
	}
	
	public void addProject(String name, String description, String homeFolder)
	{
		HopProject project = new HopProject(name, description, homeFolder);
////		project.setVersion(dateFormat.format(new Date()));
		project.setMetadataBaseFolder(Constants.DEFAULT_PROJECT_METADATA_BASE_FOLDER);
		project.setUnitTestsBasePath(Constants.DEFAULT_PROJECT_UNIT_TESTS_BASE_PATH);
		project.setDataSetsCsvFolder(Constants.DEFAULT_PROJECT_DATA_SETS_CSV_FOLDER);
		
		projects.put(name,project);
	}
	
	public void addFileToProject(TranslationFile translationFile)
	{
		String relativeFolderRoot = translationFile.getRelativeOutputRootFolder();
		if(relativeFolderRoot!=null && projects.containsKey(relativeFolderRoot))
		{
			HopProject project = projects.get(relativeFolderRoot);
			project.addTranslationFile(translationFile);
		}
		else
		{
			HopProject project = getDefaultProject();
			project.addTranslationFile(translationFile);
		}
	}
	
	public void writeProjects(String outputFolder, VelocityContext context) throws Exception
	{
		Template template = Velocity.getTemplate(Constants.ENVIRONMENT_VELOCITY_TEMPLATE);
		TemplateHandler templateHandler = new TemplateHandler(context); 
		
		for(String key : projects.keySet())
		{
			File folder = new File(outputFolder + File.separator + key);
			folder.mkdirs();
			
			HopProject project = projects.get(key);
			
			templateHandler.mergeAndWrite(project.getHomeFolder() + File.separator + "project.json", template, "project", project);
		}
	}
	
	public HopProject getProject(String name)
	{
		return projects.get(name);
	}
	
	public HopProject getDefaultProject()
	{
		return projects.get(Constants.DEFAULT_PROJECT_NAME);
	}
	
	public int getNumberOfEnvironments()
	{
		return projects.size();
	}

	public int getNumberOfTranslationFiles()
	{
		int counter = 0;
		for(String project : projects.keySet())
		{
			counter += projects.get(project).getNumberOfTranslationFiles();
		}
		return counter;
	}

	public HashMap<String, HopProject> getProjects() 
	{
		return projects;
	}

	public static SimpleDateFormat getDateFormat() 
	{
		return dateFormat;
	}

	public String getInputFolder() 
	{
		return inputFolder;
	}

	public String getOutputFolder() 
	{
		return outputFolder;
	}

	public boolean isProjectPerSubfolder() 
	{
		return projectPerSubfolder;
	}
	
}
