package com.datamelt.hop.utils;

import java.io.File;

public class FileUtils 
{
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
	
	public static boolean existFile(String folder, String filename)
	{
		return existFile(folder + "/" + filename);
	}
	
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
	
	public static void createFolder(String path) throws Exception
	{
		if(!existFolder(path))
		{
			File folder = new File(path);
			folder.mkdirs();
		}
	}
}
