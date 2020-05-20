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
import org.apache.log4j.Logger;
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
	private String hopConfigDirectory;

	private HashMap<String, String> replacements;
	private HashMap<String, Connection> databaseConnectionTypes;
	
	private VelocityContext context;
	private Template databaseTemplate;
	
	private final static Logger logger = Logger.getLogger(PdiImporter.class);
	
	public int processFile(File file) throws Exception
	{
		// parse the file
		Document document = parseDocument(file);
		
		// rename nodes according to map
		renameNodes(document);
        
        // modify the connection node and write database metadata file(s)
        int errors = processConnectionNode(document);
		
        // write document only if no errors happened when trying to
        // create the database connection files
        if(errors==0)
        {
        	File newFile = new File(newFilename);
        	if(!newFile.exists())
        	{
        		writeDocument(newFile,document);
        	}
        	else
        	{
    			errors++;
        		logger.error("file already exists - no output created: " + newFile.getName());
        	}
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
	
	private void writeDocument(File newFile,Document document) throws Exception
	{
		logger.debug("writing file: " + newFilename);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Source input = new DOMSource(document);
		Result output = new StreamResult(newFile);
		transformer.transform(input, output);
	}
	
	private void writeDatabaseMetadataFile(HashMap<String, String> connectionAttributes) throws Exception
	{
		File file = createDatabaseMetadataFile(connectionAttributes.get(Constants.TAG_CONNECTION_CHILD_NAME));
		
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
			logger.warn("no database metadata file created - file already exists: " + file.getName());
		}
	}
	
	public File createDatabaseMetadataFile(String connectionName)
	{
		File folder = new File(hopConfigDirectory + "/" + Constants.HOP_DATABASE_CONNECTIONS_FOLDER);
		folder.mkdirs();
		
		String fullFilename = hopConfigDirectory + "/" + Constants.HOP_DATABASE_CONNECTIONS_FOLDER + "/" + connectionName + ".xml";
		File file = new File(fullFilename);
		return file;
	}
	
	private void renameNodes(Document document)
	{
		for(String key:replacements.keySet())
        {
        	renameNode(document,key,replacements.get(key));
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

	public HashMap<String, String> getReplacements() 
	{
		return replacements;
	}

	public void setReplacements(HashMap<String, String> replacements) 
	{
		this.replacements = replacements;
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

	public String getHopConfigDirectory() 
	{
		return hopConfigDirectory;
	}

	public void setHopConfigDirectory(String hopConfigDirectory) 
	{
		this.hopConfigDirectory = hopConfigDirectory;
	}

	
}
