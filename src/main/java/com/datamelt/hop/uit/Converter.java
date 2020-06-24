package com.datamelt.hop.uit;

import java.util.ArrayList;

import com.datamelt.hop.utils.HopDatabaseConnection;

public interface Converter 
{
	public ArrayList<HopDatabaseConnection> getDatabaseConnections();
	
}
