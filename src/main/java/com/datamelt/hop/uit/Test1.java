package com.datamelt.hop.uit;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
//import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.datamelt.hop.utils.Constants;
import com.datamelt.hop.utils.FileUtils;
import com.datamelt.hop.utils.HopDatabaseConnection;

public class Test1 
{
	public static void main(String[] args) throws Exception
	{
		
		Properties p = new Properties();
		p.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
		p.setProperty("resource.loader.classpath.class",ClasspathResourceLoader.class.getName());
	    Velocity.init(p);
	    
	    //VelocityContext context = new VelocityContext();
	    
		String inputFolder = "/run/media/uwe/5BDB-2357/temp_pdi-to-hop2";
		String outputFolder = "/run/media/uwe/5BDB-2357/temp_pdi-to-hop-test-new-001";

		String filename =  "/run/media/uwe/5BDB-2357/temp_pdi-to-hop2/pims/afis_cfdb_compare/pims_afis_compare_staging_cfdb.ktr";
		
		Converter converter = new Converter();
		
		PdiConverter pdiConverter = converter.convertPentahoPdiFile(inputFolder, outputFolder, filename);
		pdiConverter.writeDocument();
		
		VelocityContext context = new VelocityContext();
		Template template = Velocity.getTemplate(Constants.DATABASE_METADATA_VELOCITY_TEMPLATE);
		
		File file = new File(filename);
		String fileRelativOutputFolder = FileUtils.removeLeadingFileSeparator(FileUtils.getRelativeOutputFolder(inputFolder, file.getParent()));
		String fileRelativRootFolder = FileUtils.getRootFolder(fileRelativOutputFolder);
		
		String connectionFileFolder = outputFolder + File.separator + fileRelativRootFolder + File.separator + Constants.PROJECT_METADATA_FOLDER_NAME;
		
		ArrayList<HopDatabaseConnection> connections = pdiConverter.getDatabaseConnections();
		for(HopDatabaseConnection connection : connections)
		{
			converter.writeDatabaseMetadataFile(connectionFileFolder, connection, context, template);
		}
		
		
		//HopProjectCollection projects = new HopProjectCollection(inputFolder, outputFolder, true, true);
		
		
		System.out.println();		
		
	}
}
