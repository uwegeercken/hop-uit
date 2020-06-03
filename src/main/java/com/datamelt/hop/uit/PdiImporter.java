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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.datamelt.hop.utils.Constants;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PdiImporter
{
	private String newFilename;
	private int fileType;

	private HashMap<String, String> replacementsKtrFile = Constants.getXmlKtrReplacementMap();
	private HashMap<String, String> replacementsKjbFile = Constants.getXmlKjbReplacementMap();
	private HashMap<String, String> replacementsKjbFileText = Constants.getXmlKjbTextReplacementMap();
	
	private HashMap<String, Connection> databaseConnectionTypes = Constants.getDatabaseConnectionMap();
	private VelocityContext context;
	private Template databaseTemplate;
	private static final Logger logger = LogManager.getLogger(PdiImporter.class);
	
	private String outputfolderEnvironment;
	private String outputfolderFiles;
	private String outputfolderDatabaseConnections;
	
	public int processFile(File file)
	{
		int errors = 0;
		
		// parse the file
		try
		{
			Document document = parseDocument(file);
			
			// check file type by scanning the tags
			determineFileType(document);
			
			if(fileType==Constants.FILE_TYPE_KJB)
			{
				// rename nodes according to map
				renameNodes(document);
		        
				// process text values
				processText(document);
				
		        // write document only if no errors happened when trying to
		        // create the database connection files
		        if(errors==0)
		        {
		        	File newFile = new File(getNewFilename(file.getName()));
		        	if(!newFile.exists())
		        	{
		        		writeDocument(newFile,document);
		        	}
		        	else
		        	{
		        		logger.warn("file already exists. no file generated: " + newFile.getName());
		        	}
		        }
			}
			else if(fileType==Constants.FILE_TYPE_KTR)
			{
				// rename nodes according to map
				renameNodes(document);
		        
		        // modify the connection node and write database metadata file(s)
		        processConnectionNode(document);
				
		        // write document only if no errors happened when trying to
		        // create the database connection files
		        if(errors==0)
		        {
		        	File newFile = new File(getNewFilename(file.getName()));
		        	if(!newFile.exists())
		        	{
		        		writeDocument(newFile,document);
		        	}
		        	else
		        	{
		        		logger.warn("file already exists. no file generated: " + newFile.getName());
		        	}
		        }
			}
			else
			{
				logger.warn("file is not a .kjb or .ktr file: " + file.getName());
			}
		}
		catch(Exception ex)
		{
			logger.error("the file could not be parsed: " + file.getName() + ", error: " + ex.getMessage());
			errors++;
		}
		
        return errors;
	}
	
	private Document parseDocument(File file) throws Exception
	{
		logger.debug("parsing file: " + file.getName());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
	}
	
	private void determineFileType(Document document)
	{
		// check if file has a job tag
		NodeList nodesJob = document.getElementsByTagName("job");
		NodeList nodesTransformation = document.getElementsByTagName("transformation");
		
		if(nodesJob.getLength()>0)
		{
			fileType = Constants.FILE_TYPE_KJB;
		}
		else if(nodesTransformation.getLength()>0)
		{
			fileType = Constants.FILE_TYPE_KTR;
		}
	}
	
	private void writeDocument(File newFile,Document document) throws Exception
	{
		logger.debug("writing file: " + newFilename);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Source input = new DOMSource(document);
		Result output = new StreamResult(newFile);
		transformer.transform(input, output);
	}
	
	private String getNewFilename(String filename)
	{
		if(fileType== Constants.FILE_TYPE_KJB)
		{
			return outputfolderFiles + "/" + filename.replace(Constants.PDI_JOB_FILENAME_EXTENSION, Constants.HOP_WORKFLOW_FILENAME_EXTENSION);
		}
		else
		{
			return outputfolderFiles + "/" + filename.replace(Constants.PDI_TRANSFORMATION_FILENAME_EXTENSION, Constants.HOP_PIPELINE_FILENAME_EXTENSION);
		}
	}
	
	private void writeDatabaseMetadataFile(HashMap<String, String> connectionAttributes) throws Exception
	{
		File file = new File(outputfolderDatabaseConnections + "/" + connectionAttributes.get(Constants.TAG_CONNECTION_CHILD_NAME) + ".xml");
		
		if(!file.exists())
		{
			StringWriter sw = new StringWriter();
			
			context.remove(Constants.DATABASE_METADATA_TEMPLATE_OBJECT_KEY);
			context.put( Constants.DATABASE_METADATA_TEMPLATE_OBJECT_KEY, connectionAttributes);
	
			logger.debug("merging template and connection attributes");
			databaseTemplate.merge( context, sw );
	
			
			logger.debug("writing database metadata file: " + file.getName());
			try (PrintStream out = new PrintStream(new FileOutputStream(file))) 
			{
				out.print(sw);
			}
		}
		else
		{
			logger.warn("database metadata file already exists. no file generated: " + file.getName());
		}
	}
	
	private void renameNodes(Document document)
	{
		if(fileType== Constants.FILE_TYPE_KJB)
		{
			for(String key:replacementsKjbFile.keySet())
	        {
	        	renameNode(document,key,replacementsKjbFile.get(key));
	        }
		}
		else if(fileType== Constants.FILE_TYPE_KTR)
		{
			for(String key:replacementsKtrFile.keySet())
	        {
	        	renameNode(document,key,replacementsKtrFile.get(key));
	        }
		}
		
	}
	
	private void renameNode(Document document, String from, String to)
	{
		logger.debug("renaming nodes from: [" + from + "] - to: [" + to + "]");
		NodeList nodes = document.getElementsByTagName(from);
		logger.debug("nodes found: " + nodes.getLength() );
		for (int i = 0; i < nodes.getLength(); i++) 
        {
            Node node = nodes.item(i);
            document.renameNode(node, null, to);
        }
	}
	
	private int processText(Document document) throws Exception
	{
		int errors = 0;
		NodeList nodes = document.getElementsByTagName(Constants.TAG_JOB_TAG_ACTION);
		
		for (int i = 0; i < nodes.getLength(); i++) 
        {
        	Node mainNode = nodes.item(i);
        	if (mainNode.getNodeType() == Node.ELEMENT_NODE) 
        	{
	        	Element element = (Element) mainNode;
	        	NodeList typeNode = element.getElementsByTagName(Constants.TAG_JOB_TAG_ACTION_TYPE);
	        	if(typeNode.getLength()==1)
	        	{
	        		String currentValue = typeNode.item(0).getTextContent();
	        		String replacement = replacementsKjbFileText.get(currentValue);
        			if(replacement!=null)
        			{
        				typeNode.item(0).setTextContent(replacement);
        			}
	        	}
        	}
        }
		
		return errors;
	}
	
	private int processConnectionNode(Document document) throws Exception
	{
		int errors = 0;
		ArrayList<Node> toBeRemovedNodes = new ArrayList<>();
		
		// get all connections from the document
		NodeList nodes = document.getElementsByTagName(Constants.TAG_CONNECTION);
        for (int i = 0; i < nodes.getLength(); i++) 
        {
        	Node node = nodes.item(i);
        	Element connection = (Element) node;
        	if(connection.hasChildNodes() && connection.getChildNodes().getLength()>1)
        	{
        		HashMap<String, String> connectionAttributes = new HashMap<>();

        		// type and name of the connection from the source file
        		String pdiType = connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_TYPE).item(0).getTextContent();
        		String pdiName = connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_NAME).item(0).getTextContent();

        		logger.debug("processing connection node. type: [" + pdiType + "] , name: [" + pdiName + "]");

        		if(databaseConnectionTypes.containsKey(pdiType))
        		{
	        		Connection mappedConnection = databaseConnectionTypes.get(pdiType);
	        		
	        		connectionAttributes.put(Constants.TAG_CONNECTION_CHILD_NAME, pdiName);
	        		connectionAttributes.put(Constants.DATABASE_METADATA_PLUGIN_ID, pdiType);
	        		connectionAttributes.put(Constants.DATABASE_METADATA_PLUGIN_NAME, mappedConnection.getType());
	        		
	        		connectionAttributes.put(Constants.TAG_CONNECTION_CHILD_SERVER, connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_SERVER).item(0).getTextContent());
	        		connectionAttributes.put(Constants.TAG_CONNECTION_CHILD_ACCESS_TYPE, "0");
	        		connectionAttributes.put(Constants.DATABASE_METADATA_POJO, mappedConnection.getDatabaseMeta());
	        		connectionAttributes.put(Constants.TAG_CONNECTION_CHILD_DATABASE, connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_DATABASE).item(0).getTextContent());
	        		connectionAttributes.put(Constants.TAG_CONNECTION_CHILD_PORT, connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_PORT).item(0).getTextContent());
	        		connectionAttributes.put(Constants.TAG_CONNECTION_CHILD_USERNAME, connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_USERNAME).item(0).getTextContent());
	        		connectionAttributes.put(Constants.TAG_CONNECTION_CHILD_PASSWORD, connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_PASSWORD).item(0).getTextContent());
	       		
	        		// write file
	        		writeDatabaseMetadataFile(connectionAttributes);
        	
	        		// capture which node was used - this node will be removed later from the xml document.
	        		toBeRemovedNodes.add(node);
        		}
        		else
        		{
        			errors++;
        			logger.error("unknown database type: " + pdiType);
        			logger.error("no database metadata file has been written for this type: " + pdiType);
        		}
        	}
        }
        
        // remove nodes that have been converted to metadata files for hop
        if(errors==0)
        {
	        for (int j = 0; j < toBeRemovedNodes.size(); j++) 
	        {
	        	Node toBeRemoved = toBeRemovedNodes.get(j);
	        	toBeRemoved.getParentNode().removeChild(toBeRemoved);
	        }
        }
        
        return errors;
	}

	public HashMap<String, Connection> getDatabaseConnectionTypes() 
	{
		return databaseConnectionTypes;
	}

	public void setDatabaseConnectionTypes(HashMap<String, Connection> databaseConnectionTypes) 
	{
		this.databaseConnectionTypes = databaseConnectionTypes;
	}

	public VelocityContext getContext() 
	{
		return context;
	}

	public void setVelocityContext(VelocityContext context) 
	{
		this.context = context;
	}
	
	public void setVelocityTemplate(Template databaseTemplate) 
	{
		this.databaseTemplate = databaseTemplate;
	}

	public String getNewFilename() 
	{
		return newFilename;
	}

	public void setNewFilename(String newFilename) 
	{
		this.newFilename = newFilename;
	}

	public String getOutputfolderEnvironment() 
	{
		return outputfolderEnvironment;
	}

	public void setOutputfolderEnvironment(String outputfolderEnvironment) 
	{
		this.outputfolderEnvironment = outputfolderEnvironment;
	}

	public String getOutputfolderFiles() 
	{
		return outputfolderFiles;
	}

	public void setOutputfolderFiles(String outputfolderFiles) 
	{
		this.outputfolderFiles = outputfolderFiles;
	}

	public String getOutputfolderDatabaseConnections() 
	{
		return outputfolderDatabaseConnections;
	}

	public void setOutputfolderDatabaseConnections(String outputfolderDatabaseConnections) 
	{
		this.outputfolderDatabaseConnections = outputfolderDatabaseConnections;
	}
	
}
