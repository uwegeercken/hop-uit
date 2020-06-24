package com.datamelt.hop.uit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.datamelt.hop.utils.Constants;
import com.datamelt.hop.utils.HopDatabaseConnection;

public class PdiConverter implements Converter 
{
	private File file;
	private Document document;
	
	private static final Logger logger = LogManager.getLogger(PdiConverter.class);
	
	public PdiConverter(File file) throws Exception
	{
		this.file= file;
		this.document = parseDocument(file);
		
	}
	
	@Override
	public ArrayList<HopDatabaseConnection> getDatabaseConnections() 
	{

		return null;
	}

	public int processFile()
	{
		int errors = 0;
		
		// parse the file
		try
		{
			
			ArrayList<HopDatabaseConnection> connections = processConnectionNodes();
		}
	}
	
	private Document parseDocument(File file) throws Exception
	{
		logger.debug("parsing file: " + file.getName());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
	}
	
	private ArrayList<HopDatabaseConnection> processConnectionNodes() throws Exception
	{
		int errors = 0;
		
		ArrayList<HopDatabaseConnection> databaseConnections = new ArrayList<>();
		ArrayList<Node> toBeRemovedNodes = new ArrayList<>();
		
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

        		logger.debug("processing connection node. type: [" + pdiType + "] , name: [" + pdiName + "]");

        		if(Constants.getDatabaseConnectionMap().containsKey(pdiType))
        		{
	        		Connection mappedConnection = Constants.getDatabaseConnectionMap().get(pdiType);
	        		
	        		HopDatabaseConnection databaseConnection = new HopDatabaseConnection(pdiType);
	        		databaseConnection.setName(pdiName);
	        		
	        		
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
}
