package com.datamelt.hop.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

public class TemplateHandler 
{
	private VelocityContext context;
	
	private static final Logger logger = LogManager.getLogger(TemplateHandler.class);
	
	public TemplateHandler(VelocityContext context)
	{
		this.context = context;
	}
	
	public String merge(Template template, String key, Object value)
	{
		StringWriter sw = new StringWriter();
		
		context.remove(key);
		context.put( key, value);

		logger.debug("merging variables with template: " + template.getName());
		template.merge( context, sw );
		
		return sw.toString();
	}
	
	public void mergeAndWrite(String filename, Template template, String key, Object value) throws Exception
	{
		writeFile(filename, merge(template,key,value));
	}
	
	private void writeFile(String filename, String content) throws Exception
	{
		logger.debug("writing file from template: " + filename);
		try (PrintStream out = new PrintStream(new FileOutputStream(new File(filename)))) 
		{
			out.print(content);
		}
	}
}
