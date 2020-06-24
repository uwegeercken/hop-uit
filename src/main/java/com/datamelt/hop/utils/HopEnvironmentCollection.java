package com.datamelt.hop.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.datamelt.hop.uit.PdiConverter;

public class HopEnvironmentCollection
{
	private HashMap<String, HopEnvironment> environments = new HashMap<>();
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.ENVIRONMENT_VERSION_DATE_FORMAT);
	
	private String inputFolder;
	private String outputFolder;
	private boolean environmentPerSubfolder;
	
	private ArrayList<String> subfolders = new ArrayList<>();
	private ArrayList<String> filesInInputFolder = new ArrayList<>();
	
	private static final Logger logger = LogManager.getLogger(HopEnvironmentCollection.class);
	
	
	public HopEnvironmentCollection(String inputfolder, String outputFolder, boolean environmentPerSubfolder)
	{
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
		this.environmentPerSubfolder = environmentPerSubfolder;
		
		this.filesInInputFolder = FileUtils.getFilesInInputFolder(inputfolder);
		this.subfolders = FileUtils.getSubfolders(inputfolder);
		logger.debug("number of environments: " + environments.size());
		
		createEnvironments();
	}
	
	private void createEnvironments()
	{
		// add an environment per subfolder
		if(environmentPerSubfolder)
		{
			for(String folder : subfolders)
			{
				String homeFolder = FileUtils.getFullName(outputFolder, folder);
				addEnvironment(folder, folder, homeFolder);
			}
		}
		
		// add a default environment if there are: 
		// - .ktr or .kjb files in the inputfolder
		// - or if no environments for subfolders shall be created
		if(filesInInputFolder.size()>0 || !environmentPerSubfolder)
		{
			String homeFolder = FileUtils.getFullName(outputFolder, Constants.DEFAULT_ENVIRONMENT_NAME);
			addEnvironment(Constants.DEFAULT_ENVIRONMENT_NAME, Constants.DEFAULT_ENVIRONMENT_NAME, homeFolder);
		}
	}
	
	public void addEnvironment(String name, String description, String homeFolder)
	{
		HopEnvironment environment = new HopEnvironment(name, description, homeFolder);
		environment.setVersion(dateFormat.format(new Date()));
		environment.setMetadataBaseFolder(Constants.DEFAULT_ENVIRONMENT_METADATA_BASE_FOLDER);
		environment.setUnitTestsBasePath(Constants.DEFAULT_ENVIRONMENT_UNIT_TESTS_BASE_PATH);
		environment.setDataSetsCsvFolder(Constants.DEFAULT_ENVIRONMENT_DATA_SETS_CSV_FOLDER);
		
		environments.put(name,environment);
	}
	
	public void writeEnvironments(String outputFolder, VelocityContext context) throws Exception
	{
		Template template = Velocity.getTemplate(Constants.ENVIRONMENT_VELOCITY_TEMPLATE);
		TemplateHandler templateHandler = new TemplateHandler(context); 
		
		for(String key : environments.keySet())
		{
			File folder = new File(outputFolder + File.separator + key);
			folder.mkdirs();
			
			HopEnvironment environment = environments.get(key);
			
			templateHandler.mergeAndWrite(environment.getHomeFolder() + File.separator + "environment.json", template, "environment", environment);
		}
	}
	
	public HopEnvironment getEnvironment(String name)
	{
		return environments.get(name);
	}
	
	public HopEnvironment getDefaultEnvironment()
	{
		return environments.get(Constants.DEFAULT_ENVIRONMENT_NAME);
	}
	
	public int getNumberOfEnvironments()
	{
		return environments.size();
	}

}
