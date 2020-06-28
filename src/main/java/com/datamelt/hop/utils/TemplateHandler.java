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
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

/**
 * Handles the merging and writing of files using the Apache Velocity template engine.
 * 
 * @author uwe geercken - uwe.geercken@web.de
 *
 */
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
