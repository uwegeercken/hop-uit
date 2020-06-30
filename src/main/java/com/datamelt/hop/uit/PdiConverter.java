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

import com.datamelt.hop.utils.Connection;
import com.datamelt.hop.utils.Constants;
import com.datamelt.hop.utils.FileUtils;
import com.datamelt.hop.utils.HopDatabaseConnection;
import com.datamelt.hop.utils.PdiConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

/**
 * This class is the workhorse to convert a Pentaho Data Integration (PDI) .ktr or .kjb file
 * into the equivalent Hop file.
 * 
 * Nodes are renamed, text is processed, database connections are witten to files and also
 * partial text replacements are done.
 * 
 * @author uwe geercken - uwe.geercken@web.de
 *
 */
public class PdiConverter
{
	private int fileType;

	// these all hold values that have to be replaced
	private HashMap<String, String> replacementsKtrFile 				= PdiConstants.getXmlKtrReplacementMap();
	private HashMap<String, String> replacementsKjbFile 				= PdiConstants.getXmlKjbReplacementMap();
	private HashMap<String, String> replacementsKjbFileText 			= PdiConstants.getXmlKjbTextReplacementMap();
	private HashMap<String, String> replacementsKjbFilePartialText 		= PdiConstants.getXmlKjbPartialTextReplacementMap();
	
	private String inputFolder;
	private String outputFolder;
	private String filename;
	private String outputFilename;
	private Document document;
	private ArrayList<HopDatabaseConnection> databaseConnections		= new ArrayList<HopDatabaseConnection>();
	private int errors													= 0;
	
	private static final Logger logger 									= LogManager.getLogger(PdiConverter.class);
	
	public PdiConverter(String inputfolder, String outputFolder, String filename)
	{
		this.inputFolder = inputfolder;
		this.outputFolder = outputFolder;
		this.filename = filename;
		
		outputFilename = FileUtils.getOutputFilename(inputFolder, outputFolder, filename);
	}
	
	public PdiConverter(String inputfolder, String outputFolder, String filename, boolean projectPerSubfolder)
	{
		this.inputFolder = inputfolder;
		this.outputFolder = outputFolder;
		this.filename = filename;
		
		outputFilename = FileUtils.getOutputFilename(inputFolder, outputFolder, filename, projectPerSubfolder);
	}
	
	public String getOutputFilename()
	{
		return FileUtils.translateFilename(outputFilename, fileType);
	}
	
	public void processFile()
	{
		try
		{
			// parse the file
			document = parseDocument(new File(filename));

			// check file type by scanning the tags
			determineFileType(document);
			
			if(fileType==Constants.FILE_TYPE_KJB || fileType==Constants.FILE_TYPE_KTR )
			{
					
				// rename nodes according to map
				renameNodes();
				
				// collect database connections used in the file
		        processConnectionNodes();
				
				// process text values
				processText();
				
				// process partial text values
				processPartialText();
				
			}
			else
			{
				logger.warn("file is not a .kjb or .ktr file: " + filename);
			}
			
		}
		catch(Exception ex)
		{
			logger.error("the file could not be translated: " + filename + ", error: " + ex.getMessage());
			errors++;
		}
	}
	
	/**
	 * returns the current document
	 * 
	 * @return	an XML document
	 */
	public Document getDocument()
	{
		return document;
	}
	
	/**
	 * returns all used database connections from the current document
	 * 
	 * @return	an XML document
	 */
	public ArrayList<HopDatabaseConnection> getDatabaseConnections()
	{
		return databaseConnections;
	}
	
	/**
	 * parses the defined file which is assumed to be a Pentaho PDI .ktr or .kjb XML file
	 * 
	 * @param file			the file to parse
	 * @return				an XML document
	 * @throws Exception	when document could not be parsed
	 */
	private Document parseDocument(File file) throws Exception
	{
		logger.debug("parsing file: " + file.getName());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
	}
	
	/**
	 * determine the file type by scanning the document and detecting the key nodes in the file.
	 * 
	 * @param document	a XML document
	 */
	private void determineFileType(Document document)
	{
		NodeList nodesJob = document.getElementsByTagName("job");
		NodeList nodesTransformation = document.getElementsByTagName("transformation");
		
		if(nodesJob.getLength()>0)
		{
			logger.debug("determined file type: kettle job file" );
			fileType = Constants.FILE_TYPE_KJB;
		}
		else if(nodesTransformation.getLength()>0)
		{
			logger.debug("determined file type: kettle transformation file" );
			fileType = Constants.FILE_TYPE_KTR;
		}
	}
	
	/**
	 * write the XML document to a file
	 * 
	 * @param newFile		the path and file name to create 
	 * @throws Exception	throws exception when the file can not be written
	 */
	public void writeDocument() throws Exception
	{
		File file = new File(getOutputFilename());

		if(!file.exists())
		{
			FileUtils.createFolder(file.getParent());
			
			logger.debug("writing file: " + file.getName());
	
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Source input = new DOMSource(document);
			Result output = new StreamResult(file);
			transformer.transform(input, output);
		}
		else
		{
			logger.debug("file already exists, it was not overwritten: " + file.getPath());
		}
	}
	
	/**
	 * rename the nodes in the XML document to the Hop equivalent versions 
	 * 
	 */
	private void renameNodes()
	{
		if(fileType== Constants.FILE_TYPE_KJB)
		{
			for(String key:replacementsKjbFile.keySet())
	        {
	        	renameNode(key,replacementsKjbFile.get(key));
	        }
		}
		else if(fileType== Constants.FILE_TYPE_KTR)
		{
			for(String key:replacementsKtrFile.keySet())
	        {
	        	renameNode(key,replacementsKtrFile.get(key));
	        }
		}
		
	}
	
	/**
	 * renaming nodes from a value to a defined new value
	 * 
	 * @param from	value to look for
	 * @param to	value to rename to
	 */
	private void renameNode(String from, String to)
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
	
	/**
	 * In some cases the text of a node/tag has to be replaced
	 * 
	 * @return				number of errors during processing
	 * @throws Exception	exception when node can not be processed
	 */
	private void processText() throws Exception
	{
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
	}
	
	/**
	 * in some cases partial replacement in text of a node have to be made
	 * 
	 * @return				number of errors during processing
	 * @throws Exception	exception when node can not be processed
	 */
	private void processPartialText() throws Exception
	{
		NodeList nodes = document.getElementsByTagName("*");
		
		for (int i = 0; i < nodes.getLength(); i++) 
        {
        	Node mainNode = nodes.item(i);
        	String nodeText = mainNode.getTextContent();
        	if(nodeText!=null)
        	{
        		for(String pattern : replacementsKjbFilePartialText.keySet())
        		{
        			if(nodeText.matches(".*" + pattern + ".*"))
	        		{
	        			String replacementText = replacementsKjbFilePartialText.get(pattern);
	        			String newText = nodeText.replaceAll(pattern, replacementText);
	        			mainNode.setTextContent(newText);
	        		}
        		}
        	}
        	// loop over subnodes
			Element element = (Element) mainNode;
			NodeList subNodeList = element.getElementsByTagName("*");
			if(subNodeList.getLength()>0)
			{
    			for(int j=0;j<subNodeList.getLength();j++)
    			{
    				Node subNode = subNodeList.item(j);
    				String subNodeText = subNode.getTextContent();
    				for(String pattern : replacementsKjbFilePartialText.keySet())
            		{
	        			if(subNodeText.matches(".*" + pattern + ".*"))
		        		{
		        			String replacementText = replacementsKjbFilePartialText.get(pattern);
		        			String newText = subNodeText.replaceAll(pattern, replacementText);
		        			subNode.setTextContent(newText);
		        		}
            		}
    			}
			}
        }
	}
	
	/**
	 * determines those connections which are actually used in the document by a component
	 * of the pipeline or workflow. 
	 * 
	 * @return		a set of used connections names
	 */
	private HashSet<String> getUsedConnections()
	{
		String tag;
		if(fileType==Constants.FILE_TYPE_KJB)
		{
			tag = PdiConstants.HOP_WORKFLOW_TAG_ACTION;
		}
		else
		{
			tag = PdiConstants.HOP_PIPELINE_TAG_TRANSFORM;
		}
		
		HashSet<String> connectionNames = new HashSet<String>();
		NodeList nodes = document.getElementsByTagName(tag);
		for (int i = 0; i < nodes.getLength(); i++) 
        {
        	Node node = nodes.item(i);
        	Element transform = (Element) node;
        	if(transform.hasChildNodes() && transform.getChildNodes().getLength()>1)
        	{
        		NodeList childNodes = transform.getElementsByTagName(Constants.TAG_CONNECTION);
        		if(childNodes.getLength()>0 && childNodes.item(0)!=null)
        		{
        				connectionNames.add(childNodes.item(0).getTextContent());
        		}
        	}
        }
		return connectionNames;
	}
	
	/**
	 * main method to process connection information
	 * 
	 * the files might contain one to many connections that are not used by the file in question. The usedConnections information contains
	 * the information which connections are actually used by the file. Only for those database connection files are created and finally
	 * all connection information from the file is removed. In the end it only contains references to the connection files (referenced by name).
	 * 
	 * 
	 * @param usedConnections	set of connections which are used by the file
	 * @param outputFolder		the output folder to write the database connection file to
	 * @return					number of errors during processing
	 * @throws Exception		exception when nodes can not be processed
	 */
	private void processConnectionNodes() throws Exception
	{
		ArrayList<Node> toBeRemovedNodes = new ArrayList<>();
		
		HashSet<String> usedConnections = getUsedConnections();
		
		// get all connections from the document
		NodeList nodes = document.getElementsByTagName(Constants.TAG_CONNECTION);
		
        for (int i = 0; i < nodes.getLength(); i++) 
        {
        	Node node = nodes.item(i);
        	Element connection = (Element) node;
        	if(connection.hasChildNodes() && connection.getChildNodes().getLength()>1)
        	{
        		// type and name of the connection from the source file
        		String pdiType = connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_TYPE).item(0).getTextContent();
        		String pdiName = connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_NAME).item(0).getTextContent();

        		if(usedConnections.contains(pdiName))
        		{
        		
	        		logger.debug("processing connection node. type: [" + pdiType + "] , name: [" + pdiName + "]");
	
	        		if(Constants.getDatabaseConnectionMap().containsKey(pdiType))
	        		{
		        		Connection mappedConnection = Constants.getDatabaseConnectionMap().get(pdiType);
		        		
		        		HopDatabaseConnection databaseConnection = new HopDatabaseConnection(pdiType);
		        		databaseConnection.setName(pdiName);
		        		databaseConnection.setHostname(connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_SERVER).item(0).getTextContent());
		        		databaseConnection.setAccessType(0);
		        		databaseConnection.setDatabaseName(connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_DATABASE).item(0).getTextContent());
		        		databaseConnection.setPort(connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_PORT).item(0).getTextContent());
		        		databaseConnection.setUser(connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_USERNAME).item(0).getTextContent());
		        		databaseConnection.setPassword(connection.getElementsByTagName(Constants.TAG_CONNECTION_CHILD_PASSWORD).item(0).getTextContent());
		        		databaseConnection.setPluginName(mappedConnection.getType());
		        		databaseConnection.setDriverClassName(mappedConnection.getDatabaseType());
		        		
		        		databaseConnections.add(databaseConnection);
		        		
		        		// capture which connection node was used
		        		toBeRemovedNodes.add(node);
	        		}
	        		else
	        		{
	        			errors++;
	        			logger.error("unknown database type: " + pdiType);
	        			logger.error("no database metadata file has been written for this type: " + pdiType);
	        		}
        		}
        		else
        		{
        			toBeRemovedNodes.add(node);
        		}
        	}
        }
        
        // remove nodes that have been converted to metadata files from the xml file 
        if(errors==0)
        {
	        for (int j = 0; j < toBeRemovedNodes.size(); j++) 
	        {
	        	Node toBeRemoved = toBeRemovedNodes.get(j);
	        	toBeRemoved.getParentNode().removeChild(toBeRemoved);
	        }
        }
	}

	public String getOutputFolder() 
	{
		return outputFolder;
	}

	public int getFileType() 
	{
		return fileType;
	}

	public String getInputFolder() 
	{
		return inputFolder;
	}

	public String getFilename() 
	{
		return filename;
	}

	public int getErrors() 
	{
		return errors;
	}
}
