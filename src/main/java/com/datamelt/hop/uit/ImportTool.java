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
package com.datamelt.hop.uit;

import com.datamelt.hop.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * Hop Ultimate Import Tool - Hop - UIT
 * 
 * Tool to convert Pentaho Data Integration (PDI) .ktr files to the Project-Hop .hpl file format. Pentaho PDI files are XML files.
 * 
 * Project Hop provides a gui for designing ETL flows, several client programs and a server component. As Hop is a fork of the PDI
 * tool at some point in time, the file format is largely the same. But for various reasons some parts are different.
 * 
 * What is converted:
 * 
 * Tags:
 * Some tags are different between the .ktr and the .hpl XML file format (structure of the XML file is not changed)
 * 
 * Database Connections:
 * Database connections are embedded in the PDI XML file. Hop externalizes this information into metadata files and stores them in a folder
 * hierarchy.
 * 
 * Note:
 * Database connections are identified by their name in the PDI XML file. Consequently a Hop metadata file is written with this name, if it
 * does not already exist. If it exists, no Hop metadata file is written.
 * 
 * If the converted file in the output folder already exists, then the existing file is not overwritten.
 * 
 * @author uwe geercken
 *
 */
public class ImportTool 
{
	
	private static final String version 		= "0.1.3";
	private static final String versionDate 	= "2020-05-23";
	
	private static String inputfolder;
	private static String outputfolder;
	private static String outputfolderEnvironment;
	private static String outputfolderFiles;
	private static String outputfolderDatabaseConnections;
	
	private static VelocityContext context;
	
	private static ArrayList<String> inputFilenames = new ArrayList<>();
	
	private final static Logger logger = Logger.getLogger(ImportTool.class);
	
	public static void main(String[] args) throws Exception
	{
		int numberOfFilesWithErrors = 0;
		int numberOfErrorsTotal=0;
		
		if(args.length<2 || args[0].equals("--help") || args[0].equals("-h"))
		{
			help();
		}
		else
		{
			logger.info(getVersionInfo());
			
			PdiImporter importer = new PdiImporter();
			processArguments(importer, args);

			outputfolderEnvironment = outputfolder + "/" + Constants.FOLDER_ENVIRONMENT;
			outputfolderFiles = outputfolder + "/" + Constants.FOLDER_FILES;
			outputfolderDatabaseConnections = outputfolderEnvironment + "/" + Constants.HOP_METASTORE_FOLDER + "/" + Constants.HOP_DATABASE_CONNECTIONS_FOLDER;
			
			context = getVelocityContext();
			
			importer.setOutputfolderEnvironment(outputfolderEnvironment);
			importer.setOutputfolderFiles(outputfolderFiles);
			importer.setOutputfolderDatabaseConnections(outputfolderDatabaseConnections);
			
			// get an apache Velocity context we can use
			importer.setVelocityContext(context);
			
			// template comes from the classpath
			Template databaseTemplate = Velocity.getTemplate(Constants.DATABASE_METADATA_VELOCITY_TEMPLATE);
			importer.setVelocityTemplate(databaseTemplate);

			// we need an inputfolder where all files to process are located
			if(inputfolder != null && outputfolder!=null)
			{
				logger.info("processing files from: " + inputfolder);
				logger.info("output files to: " + outputfolder);
				
				// create the output folder if not present
				createFolder(outputfolderEnvironment);
				createFolder(outputfolderFiles);
				createFolder(outputfolderDatabaseConnections);
				
				logger.debug("creating .type.xml metadata file for database connections in folder: " + outputfolderDatabaseConnections);
				createTypeFile(outputfolderDatabaseConnections, Constants.HOP_DATABASE_CONNECTIONS_FOLDER);
				
				// create a default environment file
				logger.debug("creating environment metadata file in folder: " + outputfolderEnvironment);
				createEnvironment();
				
				// array of files to process
				File[] files = null;
				
				// if no file names are specified then we process all files in the input folder
				if(inputFilenames.size() == 0)
				{
					File folder = new File(inputfolder);
					if(folder.exists() && folder.canRead())
					{
						 files = folder.listFiles();
						logger.info("files to process: " + files.length);
					}
				}
				// if we have one or multiple file names
				else
				{
					files = new File[inputFilenames.size()];
					for(int i=0;i<inputFilenames.size();i++)
					{
						File file = new File(inputfolder + "/" + inputFilenames.get(i));
						if(file.exists() && file.canRead())
						{
							files[i] = file;
						}
						else
						{
							logger.info("file does not exist or cannot be read: " + file.getName());
						}
					}
				}

				// loop over files and process them
				for(int i=0;i<files.length;i++)
				{
					int errors = importer.processFile(files[i]);
					numberOfErrorsTotal = numberOfErrorsTotal + errors;
					if(errors>0)
					{
						numberOfFilesWithErrors ++;
						logger.error("file not converted: " + files[i].getName() + ", errors in file: " + errors);
					}
					else
					{
						logger.debug("file converted: " + files[i].getName());
					}
				}
			}
			else
			{
				logger.error("inputfolder and outputfolder must be specified");
			}
		}
		
		logger.info("number of files with errors: " + numberOfFilesWithErrors);
		logger.info("number of total errors: " + numberOfErrorsTotal);
		logger.info("processing complete");
	}
	
	/**
	 * process all given arguments.
	 * 
	 * @param importer	the importer to provide the arguments to
	 * @param args		the arguments passed to this program
	 */
	private static void processArguments(PdiImporter importer, String[] args)
	{
		logger.debug("process arguments from: " + Arrays.asList(args));
		for(int i=0;i<args.length;i++)
		{
			if(args[i].startsWith("-i="))
			{
				inputfolder = args[i].substring(3);
			}
			else if(args[i].startsWith("-o="))
			{
				outputfolder = args[i].substring(3);
			}
			else if(args[i].startsWith("-f="))
			{
				inputFilenames.add(args[i].substring(3));
			}
		}
	}
	
	/**
	 * create a folder and all parent folder, if they don't exist.
	 * 
	 * @param folder  name of the folder to create
	 */
	private static void createFolder(String folder)
	{
		File f = new File(folder);
		f.mkdirs();
	}
	
	/**
	 * create a folder and all parent folder, if they don't exist.
	 * 
	 * @param folder  name of the folder to create
	 */
	private static void createTypeFile(String folder, String typeName) throws Exception
	{
		StringWriter sw = new StringWriter();
		Template typeFileTemplate = Velocity.getTemplate(Constants.TYPEFILE_VELOCITY_TEMPLATE);
		
		context.put("typename",typeName);
		context.put("typedescription","Metadata for: " + typeName);
		
		logger.debug("merging template and type file attributes");
		typeFileTemplate.merge( context, sw );
		
		File file = new File(folder + "/.type.xml");
		
		logger.debug("writing environment metadata file: " + file.getName());
		try (PrintStream out = new PrintStream(new FileOutputStream(file))) 
		{
			out.print(sw);
		}
	}
	
	/**
	 * create a folder and all parent folder, if they don't exist.
	 * 
	 * @param folder  name of the folder to create
	 */
	private static void createEnvironment() throws Exception
	{
		StringWriter sw = new StringWriter();
		
		Template environmentTemplate = Velocity.getTemplate(Constants.ENVIRONMENT_VELOCITY_TEMPLATE);
		
		context.put("HOP_ENVIRONMENT","environment");
		context.put("HOP_ENVIRONMENT_HOME_FOLDER",outputfolderEnvironment);
		
		logger.debug("merging template and environment attributes");
		environmentTemplate.merge( context, sw );
		
		File file = new File(outputfolder + "/default_environment.xml");
		
		logger.debug("writing environment metadata file: " + file.getName());
		try (PrintStream out = new PrintStream(new FileOutputStream(file))) 
		{
			out.print(sw);
		}
		
	}
	
	private static String getVersionInfo()
	{
		return "Version " + version + ", last update: " + versionDate; 
	}
	
	/**
	 * returns an Apache Velocity Context. This context is used for all transformations
	 * with the velocity template engine - we just need one instance.
	 * 
	 * @return	Apache Velocity Context
	 */
	private static VelocityContext getVelocityContext()
	{
		Properties p = new Properties();
		p.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
		p.setProperty("resource.loader.classpath.class",ClasspathResourceLoader.class.getName());
	    Velocity.init(p);
		
		return new VelocityContext();
	}
	
	/**
	 * provide some help to the user, if the number of arguments is insufficient or the first
	 * argument is either --help or -h
	 * 
	 */
	private static void help()
	{
		System.out.println("ImportTool " + getVersionInfo()); 
		System.out.println("Program to convert files created by the Pentaho Data Integration tool (PDI) into the HOP format.");
		System.out.println("Files are read from the input folder, converted and output to the output folder. If a file name is");
		System.out.println("specified, this file is processed from the input folder. Multiple file names may be specified by");
		System.out.println("repeating the -f argument. If no file name is specified then all files in the input folder are processed.");
    	System.out.println();
    	System.out.println("Files are not overwritten in case they already exist");
    	System.out.println();
    	System.out.println("ImportTool -i=[inputfolder] -o=[outputfolder] -f=[file name]");
    	System.out.println("where [inputfolder]          : required. path to the folder where the ktr files are located");
    	System.out.println("      [outputfolder]         : required. path to the folder where the hpl files are output to");
    	System.out.println("      [file name]            : optional. name of a .ktr file to convert - can be specified multiple times");
    	System.out.println();
    	System.out.println("example: ImportTool -i=/home/me/input -o=/home/me/output");
    	System.out.println("       : ImportTool -i=/home/me/input -o=/home/me/output -f=myfile.ktr");
    	System.out.println("       : ImportTool -i=/home/me/input -o=/home/me/output -f=myfile1.ktr -f=myfile2.ktr");
    	System.out.println();
    	System.out.println("published as open source under the Apache License. read the licence notice.");
    	System.out.println("check https://github.com/uwegeercken for more");
    	System.out.println("all code by uwe geercken, 2020. uwe.geercken@web.de");
    	System.out.println();
	}
	
}
