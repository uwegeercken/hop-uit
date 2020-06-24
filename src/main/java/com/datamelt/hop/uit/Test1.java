package com.datamelt.hop.uit;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.datamelt.hop.utils.FileUtils;
import com.datamelt.hop.utils.HopEnvironmentCollection;
import com.datamelt.hop.utils.TemplateHandler;

public class Test1 
{
	public static void main(String[] args) throws Exception
	{
		
		Properties p = new Properties();
		p.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
		p.setProperty("resource.loader.classpath.class",ClasspathResourceLoader.class.getName());
	    Velocity.init(p);
	    
	    VelocityContext context = new VelocityContext();
	    
		String inputfolder = "/run/media/uwe/5BDB-2357/temp_pdi-to-hop";
		String outputfolder = "/run/media/uwe/5BDB-2357/temp_pdi-to-hop-test-new-001";
		
		HopEnvironmentCollection environments = new HopEnvironmentCollection(inputfolder,outputfolder, true);
		
		environments.writeEnvironments(outputfolder,context);
		
	}
}
