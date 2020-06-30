package com.datamelt.hop.uit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.datamelt.hop.utils.Constants;
import com.datamelt.hop.utils.FileUtils;
import com.datamelt.hop.utils.HopDatabaseConnection;

public class Converter 
{
	private static final Logger logger 								= LogManager.getLogger(Converter.class);
	
	public PdiConverter convertPentahoPdiFile(String inputFolder,String outputFolder, String filename)
	{
		PdiConverter converter = new PdiConverter(inputFolder, outputFolder, filename);
		converter.processFile();
		return converter;
	}
	
	public PdiConverter convertPentahoPdiFile(String inputFolder,String outputFolder, String filename, boolean projectPerSubfolder)
	{
		PdiConverter converter = new PdiConverter(inputFolder, outputFolder, filename, projectPerSubfolder);
		converter.processFile();
		return converter;
	}
	
	/**
	 * writes a database connection file to the filesystem
	 * 
	 * @param databaseConnection	the database connection object to use
	 * @param outputFolder			the folder to write to
	 * @throws Exception			exception when the file can not be written
	 */
	public void writeDatabaseMetadataFile(String outputFolder, HopDatabaseConnection databaseConnection, VelocityContext context, Template template ) throws Exception
	{
		FileUtils.createFolder(outputFolder);
		
		String filename = databaseConnection.getName();
		// correct the filename if it contains invalid characters
		String correctedFilename = FileUtils.correctInvalidFilename(filename);
		
		File file = new File(outputFolder + File.separator + correctedFilename + ".json");
		if(!file.exists())
		{
			StringWriter sw = new StringWriter();
			
			context.remove(Constants.DATABASE_METADATA_TEMPLATE_OBJECT_KEY);
			context.put( Constants.DATABASE_METADATA_TEMPLATE_OBJECT_KEY, databaseConnection);
	
			template.merge( context, sw );
			logger.debug("writing database connection file: " + file.getName());
			try (PrintStream out = new PrintStream(new FileOutputStream(file))) 
			{
				out.print(sw);
			}
		}
		else
		{
			logger.debug("database metadata file already exists. no file generated: " + file.getName());
		}
	}
	
}
