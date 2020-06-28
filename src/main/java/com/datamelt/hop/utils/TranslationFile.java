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
/**
 * This class holds the metadata information for a file that is to be translated.
 * 
 * 
 * @author uwe geercken - uwe.geercken@web.de
 *
 */
public class TranslationFile 
{
	private String fileParentFolder;
	private String pathAndFilename;
	private String filename;
	private String relativeOutputFolder;
	private String relativeOutputRootFolder;
	

	public TranslationFile(File file, String relativeOutputFolder)
	{
		this.pathAndFilename = file.getPath();
		this.filename = file.getName();
		this.fileParentFolder = file.getParent();
		if(!relativeOutputFolder.equals(""))
		{
			this.relativeOutputFolder = FileUtils.removeLeadingFileSeparator(relativeOutputFolder);
			this.relativeOutputRootFolder = FileUtils.getRootFolder(this.relativeOutputFolder);
		}
	}
	
	public String getFileParentFolder() 
	{
		return fileParentFolder;
	}

	public String getFilename() 
	{
		return filename;
	}

	public String getRelativeOutputFolder() 
	{
		return relativeOutputFolder;
	}

	public String getPathAndFilename() 
	{
		return pathAndFilename;
	}

	public String getRelativeOutputRootFolder() 
	{
		return relativeOutputRootFolder;
	}
	
	public String getMetadataFolder()
	{
		return relativeOutputRootFolder + File.separator + Constants.PROJECT_METADATA_FOLDER_NAME;
	}
	
}
