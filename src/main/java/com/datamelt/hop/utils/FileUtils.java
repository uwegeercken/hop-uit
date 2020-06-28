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
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Class with several helper methods to simplify file or folder handling
 * 
 * @author uwe geercken - uwe.geercken@web.de
 *
 */
public class FileUtils 
{
	public static String getFullName(String path, String filename)
	{
		return path + "/" + filename;
	}
	
	/**
	 * check if a given folder exists
	 * 
	 * @param path to the folder
	 * @return if the folder exists and is a directory
	 */
	public static boolean existFolder(String path)
	{
		try
		{
			File folder = new File(path);

			if(folder.exists() && folder.isDirectory())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	/**
	 * checks if a file exists in a given folder
	 * 
	 * @param folder	path to the folder
	 * @param filename	name of the file
	 * @return			if the file exists
	 */
	public static boolean existFile(String folder, String filename)
	{
		return existFile(folder + "/" + filename);
	}
	
	/**
	 * checks if a file exists
	 * 
	 * @param filename	name of the file
	 * @return			if the file exists
	 */
	public static boolean existFile(String filename)
	{
		try
		{
			File file = new File(filename);

			if(file.exists() && file.isFile())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	/**
	 * create a folder for the given path
	 * 
	 * @param path			path to the folder
	 * @throws Exception	exception if the folder can not be created
	 */
	public static void createFolder(String path) throws Exception
	{
		if(!existFolder(path))
		{
			File folder = new File(path);
			folder.mkdirs();
		}
	}
	
	/**
	 * remove the filename part from the path and then 
	 * create the relevant folder
	 * 
	 * @param path			path to the folder
	 * @param filename		name of the file
	 * @throws Exception	exception if the folder can not be created
	 */
	public static void createFolder(String path, String filename) throws Exception
	{
		String fullPath = path.replaceAll("/" + filename, "");
				
		if(!existFolder(fullPath))
		{
			File folder = new File(fullPath);
			folder.mkdirs();
		}
	}
	
	/**
	 * textual information from the xml file is used to create metadata files. this information
	 * might contain characters that are not suitable for a filename, so they need to be replaced
	 * with valid ones
	 * 
	 * 
	 * @param filename	the string that shall be used as the filename
	 * @return			a corrected version of the filename
	 */
	public static String correctInvalidFilename(String filename)
	{
		for(String character : Constants.getInvalidFilenameCharacterReplacement().keySet())
		{
			filename = filename.replaceAll(Pattern.quote(character), Constants.getInvalidFilenameCharacterReplacement().get(character));
		}
		return filename;
	}
	
	/**
	 * translate Pentaho PDI .ktr and .kjb filenames to the equivalent Hop ones
	 * 
	 * @param 	filename name of the file
	 * @param 	fileType indicating which type the file is
	 * @return	the translated filename
	 */
	public static String migrateFilename(String filename, int fileType)
	{
		if(fileType == Constants.FILE_TYPE_KJB)
		{
			return filename.replace(Constants.PDI_JOB_FILENAME_EXTENSION, Constants.HOP_WORKFLOW_FILENAME_EXTENSION);
		}
		else if(fileType == Constants.FILE_TYPE_KTR)
		{
			return filename.replace(Constants.PDI_TRANSFORMATION_FILENAME_EXTENSION, Constants.HOP_PIPELINE_FILENAME_EXTENSION);
		}
		else
		{
			return filename;
		}
	}
	
	/**
	 * for a given project and translationfile determine the correct output folder
	 * 
	 * @param project			the relevant project
	 * @param translationFile	the relevant translation file
	 * @return					name of the output folder
	 */
	public static String getFileOutputFolder(String outputFolder, HopProject project, TranslationFile translationFile)
	{
		String newFolder = outputFolder + File.separator + project.getName() ; 
		if(translationFile.getRelativeOutputFolder()!=null)
		{
			newFolder = newFolder + File.separator + translationFile.getRelativeOutputFolder();
		}
		return newFolder;
	}
	
	public static String getRelativeOutputFolder(String inputFolder, String fileParentFolder)
	{
		return fileParentFolder.replaceFirst(inputFolder, "");
	}
	
	public static String getRootFolder(String folder)
	{
		if(folder!=null)
		{
			if(folder.startsWith(File.separator))
			{
				int firstPos = folder.indexOf(File.separator);
				int secondPos = folder.indexOf(File.separator,firstPos+1);
				if(secondPos>-1)
				{
					return folder.substring(0,secondPos);
				}
				else
				{
					return folder;
				}
			}
			else
			{
				int firstPos = folder.indexOf(File.separator);
				if(firstPos>-1)
				{
					return folder.substring(0,firstPos);
				}
				else
				{
					return folder;
				}
			}
		}
		else
		{
			return folder;
		}
		
	}
	
	public static String removeLeadingFileSeparator(String folder)
	{
		if(folder.startsWith(File.separator))
		{
			return folder.substring(1);
		}
		else
		{
			return folder;
		}
	}
	
	public static ArrayList<String> getSubfolders(String folder)
	{
		ArrayList<String> allSubfolders = new ArrayList<>();
		File mainFolder = new File(folder);
		if(mainFolder.isDirectory())
		{
			File[] files = mainFolder.listFiles();
			for(File folderFile : files)
			{
				if(folderFile.isDirectory() && folderFile.canRead())
				{
					allSubfolders.add(folderFile.getName());
				}
			}
		}
		return allSubfolders;
	}
	
	/**
	 * recursively go over a list of files or folders and collect the
	 * files that need to be processed
	 * 
	 * @param file		a file or folder
	 * @param allFiles	array list of files collected
	 */
	public static void traverseFilesystem(String inputFolder, File file, ArrayList<TranslationFile> allFiles)
	{
		// if we have a directory, recursively loop over files or folders
		if(file.isDirectory())
		{
			File[] files = file.listFiles();
			for(File folderFile : files)
			{
				traverseFilesystem(inputFolder, folderFile, allFiles);
			}
		}
		// if we have a file and we can read it and it is relevant
		else
		{
			if(file.canRead() && (file.getName().endsWith(Constants.PDI_JOB_FILENAME_EXTENSION)|| file.getName().endsWith(Constants.PDI_TRANSFORMATION_FILENAME_EXTENSION)))
			{
				String relativeOutputFolder = FileUtils.getRelativeOutputFolder(inputFolder, file.getParent());
				allFiles.add(new TranslationFile(file, relativeOutputFolder));
			}
		}
	}
}
