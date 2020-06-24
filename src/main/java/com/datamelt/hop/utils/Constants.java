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

import java.util.HashMap;

import com.datamelt.hop.uit.Connection;

public class Constants 
{
	public static final String HOP_SYSTEM_VARIABLES_PREFIX						= "HOP_";
	public static final String HOP_SYSTEM_VARIABLE_CONFIG_DIRECTORY				= "HOP_CONFIG_DIRECTORY";
	
	public static final String TAG_CONNECTION									= "connection";
	
	public static final String TAG_CONNECTION_CHILD_NAME						= "name"; 
	public static final String TAG_CONNECTION_CHILD_SERVER						= "server";
	public static final String TAG_CONNECTION_CHILD_TYPE						= "type";
	public static final String TAG_CONNECTION_CHILD_ACCESS_TYPE					= "access";
	public static final String TAG_CONNECTION_CHILD_DATABASE					= "database";
	public static final String TAG_CONNECTION_CHILD_PORT						= "port";
	public static final String TAG_CONNECTION_CHILD_USERNAME					= "username";
	public static final String TAG_CONNECTION_CHILD_PASSWORD					= "password";
	
	public static final String TAG_JOB_TAG_ACTION								= "action";
	public static final String TAG_JOB_TAG_ACTION_TYPE							= "type";
	
	public static final String TAG_JOB_TAG_ENTRY								= "entry";
	
	public static final String DATABASE_METADATA_TEMPLATE_OBJECT_KEY 			= "connection";
	
	public static final String HOP_TYPE_FILE_DATABASES_TAG_NAME_VALUE			= "Relational Database Connection";
	public static final String HOP_TYPE_FILE_ENVIRONMENTS_TAG_NAME_VALUE		= "Hop Environment";
	public static final String HOP_TYPE_FILE_ENVIRONMENTS_FILENAME				= "hop-uit-default.xml";
	public static final String HOP_TYPE_FILE_FILENAME							= ".type.xml";
	
	public static final String HOP_METASTORE_FOLDER			 					= "metastore";
	public static final String HOP_DATABASE_CONNECTIONS_FOLDER 					= "Relational Database Connection";

	public static final String PDI_TRANSFORMATION_FILENAME_EXTENSION 			= ".ktr";
	public static final String HOP_PIPELINE_FILENAME_EXTENSION 					= ".hpl";
	public static final String PDI_JOB_FILENAME_EXTENSION 						= ".kjb";
	public static final String HOP_WORKFLOW_FILENAME_EXTENSION 					= ".hwf";

	
	// loading from classpath
	public static final String DATABASE_METADATA_VELOCITY_TEMPLATE				= "/templates/database.xml.template";
	public static final String ENVIRONMENT_VELOCITY_TEMPLATE					= "/templates/environment.json.template";
	public static final String TYPEFILE_VELOCITY_TEMPLATE						= "/templates/type.xml.template";
	
	public static final String DATABASE_METADATA_PLUGIN_ID						= "pluginid";
	public static final String DATABASE_METADATA_PLUGIN_NAME					= "pluginname";
	public static final String DATABASE_METADATA_POJO							= "pojo";
	
	public static final String DATABASE_TYPE_APACHE_DERBY						= "DERBY";
	public static final String DATABASE_TYPE_AS400								= "AS/400";
	public static final String DATABASE_TYPE_BORLAND_INTERBASE					= "INTERBASE";
	public static final String DATABASE_TYPE_CALPOINT_INFINIDB					= "INFINIDB";
	public static final String DATABASE_TYPE_DB2								= "DB2";
	public static final String DATABASE_TYPE_DBASE								= "DBASE";
	public static final String DATABASE_TYPE_EXASOL4							= "EXASOL4";
	public static final String DATABASE_TYPE_FIREBIRD							= "FIREBIRD";
	public static final String DATABASE_TYPE_GENERIC							= "GENERIC";
	public static final String DATABASE_TYPE_GOOGLEBIGQUERY						= "GOOGLEBIGQUERY";
	public static final String DATABASE_TYPE_GREENPLUM							= "GREENPLUM";
	public static final String DATABASE_TYPE_GUPTA_SQL_BASE						= "SQLBASE";
	public static final String DATABASE_TYPE_H2									= "H2";
	public static final String DATABASE_TYPE_HYPERSONIC							= "HYPERSONIC";
	public static final String DATABASE_TYPE_INFOBRIGHT							= "INFOBRIGHT";
	public static final String DATABASE_TYPE_INFORMIX							= "INFORMIX";
	public static final String DATABASE_TYPE_INGRES								= "INGRES";
	public static final String DATABASE_TYPE_INGRES_VECTORWISE					= "VECTORWISE";
	public static final String DATABASE_TYPE_INTERSYSTEMS_CACHE					= "CACHE";
	public static final String DATABASE_TYPE_KINGBASEES							= "KINGBASEES";
	public static final String DATABASE_TYPE_SAPDB								= "SAPDB";
	public static final String DATABASE_TYPE_MARIADB							= "MARIADB";
	public static final String DATABASE_TYPE_MONETDB							= "MONETDB";
	public static final String DATABASE_TYPE_MSACCESS							= "MSACCESS";
	public static final String DATABASE_TYPE_MSSQL								= "MSSQL";
	public static final String DATABASE_TYPE_MSSQLNATIVE						= "MSSQLNATIVE";
	public static final String DATABASE_TYPE_MYSQL								= "MYSQL";
	public static final String DATABASE_TYPE_NETEZZA							= "NETEZZA";
	public static final String DATABASE_TYPE_NONE								= "NONE";
	public static final String DATABASE_TYPE_ORACLE								= "ORACLE";
	public static final String DATABASE_TYPE_ORACLERDB							= "ORACLERDB";
	public static final String DATABASE_TYPE_POSTGRESQL							= "POSTGRESQL";
	public static final String DATABASE_TYPE_REDSHIFT							= "REDSHIFT";
	public static final String DATABASE_TYPE_REMEDY_AR_SYSTEM					= "REMEDY-AR-SYSTEM";
	public static final String DATABASE_TYPE_SNOWFLAKE							= "SNOWFLAKE";
	public static final String DATABASE_TYPE_SQLITE								= "SQLITE";
	public static final String DATABASE_TYPE_SYBASE								= "SYBASE";
	public static final String DATABASE_TYPE_SYBASEIQ							= "SYBASEIQ";
	public static final String DATABASE_TYPE_TERADATA							= "TERADATA";
	public static final String DATABASE_TYPE_UNIVERSE							= "UNIVERSE";
	public static final String DATABASE_TYPE_VERTICA							= "VERTICA";
	public static final String DATABASE_TYPE_VERTICA5							= "VERTICA5";
	
	public static final String HOP_UIT_FOLDER_ENVIRONMENT						= "hop-uit-environment";
	public static final String HOP_UIT_ENVIRONMENT								= "hop-uit-environment";
	public static final String HOP_CONFIG_FOLDER_ENVIRONMENTS					= "environments";
	public static final String HOP_CONFIG_FOLDER_ENVIRONMENT					= "Hop Environment";
	public static final String FOLDER_FILES										= "files";
	
	public static final int FILE_TYPE_KJB										= 0;
	public static final int FILE_TYPE_KTR										= 1;
	
	public static final String BOOLEAN_TRUE_AS_STRING							= "true";
	public static final String BOOLEAN_FALSE_AS_STRING							= "false";
	
	public static final String DEFAULT_ENVIRONMENT_NAME							= "default-hop-uit";
	public static final String DEFAULT_ENVIRONMENT_METADATA_BASE_FOLDER			= "${ENVIRONMENT_HOME}/metadata";
	public static final String DEFAULT_ENVIRONMENT_UNIT_TESTS_BASE_PATH			= "${ENVIRONMENT_HOME}";
	public static final String DEFAULT_ENVIRONMENT_DATA_SETS_CSV_FOLDER			= "${ENVIRONMENT_HOME}/datasets";
	
	public static final String ENVIRONMENT_VERSION_DATE_FORMAT					= "yyyy-MM-dd";
	
	/**
	 * returns a map of invalid characters in filenames and their replacement characters.
	 * 
	 * @return	map of characters and their replacements
	 */
	public static HashMap<String, String> getInvalidFilenameCharacterReplacement()
	{
		HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("<", "_");
	    replacements.put(">", "_");
	    replacements.put(":", "_");
	    replacements.put("*", "_");
	    replacements.put("\\", "_");
	    replacements.put("/", "_");
	    replacements.put(";", "_");
	    replacements.put(",", "_");
	    replacements.put("|", "_");
	    
	    return replacements;
	}
	
	/**
	 * returns a map of replacements between the Kettle/PDI format of a transformation and the Hop format of a pipeline 
	 * 
	 * @return	map of tag names and their replacements
	 */
	public static HashMap<String, String> getXmlKtrReplacementMap()
	{
		HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("transformation", "pipeline");
	    replacements.put("trans_type", "pipeline_type");
	    replacements.put("trans_status", "pipeline_status");
	    replacements.put("step", "transform");
	    replacements.put("step_error_handling", "transform_error_handling");
	    
	    return replacements;
	}
	
	/**
	 * returns a map of replacements between the Kettle/PDI format of a job and the Hop format of a workflow 
	 * 
	 * @return map of tag names and their replacements
	 */
	public static HashMap<String, String> getXmlKjbReplacementMap()
	{
		HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("job", "workflow");
	    replacements.put("job_version", "workflow_version");
	    replacements.put("entries", "actions");
	    replacements.put("entry", "action");
	    replacements.put("job-log-table", "workflow-log-table");
	    
	    return replacements;
	}
	
	/**
	 * returns a map of node text replacements between the Kettle/PDI format of a job and the Hop format of a workflow 
	 * 
	 * @return map of xml texts and their replacements
	 */
	public static HashMap<String, String> getXmlKjbTextReplacementMap()
	{
		HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("TRANS", "PIPELINE");
	    
	    return replacements;
	}
	
	/**
	 * returns a map of partial node text replacements between the Kettle/PDI format of a job and the Hop format of a workflow 
	 * 
	 * @return map of partial xml texts and their replacements
	 */
	public static HashMap<String, String> getXmlKjbPartialTextReplacementMap()
	{
		HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("Internal.Job", "Internal.Workflow");
	    replacements.put("Internal.Transformation", "Internal.Pipeline");
	    replacements.put(".ktr", ".hpl");
	    replacements.put(".kbj", ".hwf");
	    
	    return replacements;
	}
	
	/**
	 * map of database types (key) and their names and classes 
	 * 
	 * the value is a Connection object containing the name and the actual class.
	 * 
	 * 
	 * @return map of database type mappings
	 */
	public static HashMap<String, Connection> getDatabaseConnectionMap()
	{
		HashMap<String, Connection> connectionsMap = new HashMap<>();
		
		connectionsMap.put(DATABASE_TYPE_APACHE_DERBY, new Connection("Apache Derby","org.apache.hop.databases.derby.DerbyDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_AS400, new Connection("AS/400","org.apache.hop.databases.as400.AS400DatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_BORLAND_INTERBASE, new Connection("Borland Interbase","org.apache.hop.databases.interbase.InterbaseDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_CALPOINT_INFINIDB, new Connection("Calpont InfiniDB","org.apache.hop.databases.infinidb.InfiniDbDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_DB2, new Connection("DB2","org.apache.hop.databases.db2.DB2DatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_DBASE, new Connection("dBase III, IV or 5","org.apache.hop.databases.dbase.DbaseDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_EXASOL4, new Connection("Exasol 4","org.apache.hop.databases.exasol4.Exasol4DatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_FIREBIRD, new Connection("Firebird SQL","org.apache.hop.databases.firebird.FirebirdDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_GENERIC, new Connection("Generic database","org.apache.hop.core.database.GenericDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_GOOGLEBIGQUERY, new Connection("Google BigQuery","org.apache.hop.databases.googlebigquery.GoogleBigQueryDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_GREENPLUM, new Connection("Greenplum","org.apache.hop.databases.greenplum.GreenplumDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_GUPTA_SQL_BASE, new Connection("Gupta SQL Base","org.apache.hop.databases.sqlbase.GuptaDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_H2, new Connection("H2","org.apache.hop.databases.h2.H2DatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_HYPERSONIC, new Connection("Hypersonic","org.apache.hop.databases.hypersonic.HypersonicDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_INFOBRIGHT, new Connection("Infobright","org.apache.hop.databases.infobright.InfobrightDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_INFORMIX, new Connection("Informix","org.apache.hop.databases.informix.InformixDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_INGRES, new Connection("Ingres","org.apache.hop.databases.ingres.IngresDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_INGRES_VECTORWISE, new Connection("Ingres VectorWise","org.apache.hop.databases.vectorwise.VectorWiseDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_INTERSYSTEMS_CACHE, new Connection("Intersystems Cache","org.apache.hop.databases.cache.CacheDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_KINGBASEES, new Connection("KingbaseES","org.apache.hop.databases.kingbasees.KingbaseESDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_MARIADB, new Connection("MariaDB","org.apache.hop.databases.mariadb.MariaDBDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_SAPDB, new Connection("MaxDB (SAP DB)","org.apache.hop.databases.sapdb.SAPDBDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_MONETDB, new Connection("MonetDB","org.apache.hop.databases.monetdb.MonetDBDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_MSACCESS, new Connection("MS Access","org.apache.hop.databases.msaccess.MSAccessDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_MSSQL, new Connection("MS SQL Server","org.apache.hop.databases.mssql.MsSqlServerDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_MSSQLNATIVE, new Connection("MS SQL Server (Native)","org.apache.hop.databases.mssqlnative.MsSqlServerNativeDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_MYSQL, new Connection("MySQL","org.apache.hop.databases.mysql.MySqlDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_NETEZZA, new Connection("Netezza","org.apache.hop.databases.netezza.NetezzaDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_NONE, new Connection("No connection type","org.apache.hop.core.database.NoneDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_ORACLE, new Connection("Oracle","org.apache.hop.databases.oracle.OracleDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_ORACLERDB, new Connection("Oracle RDB","org.apache.hop.databases.oraclerdb.OracleRDBDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_POSTGRESQL, new Connection("PostgreSQL","org.apache.hop.databases.postgresql.PostgreSqlDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_REDSHIFT, new Connection("Redshift","org.apache.hop.databases.redshift.RedshiftDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_REMEDY_AR_SYSTEM, new Connection("Remedy Action Request System","org.apache.hop.databases.remedyarsystem.RemedyActionRequestSystemDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_SNOWFLAKE, new Connection("Snowflake","org.apache.hop.databases.snowflake.SnowflakeDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_SQLITE, new Connection("SQLite","org.apache.hop.databases.sqlite.SqliteDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_SYBASE, new Connection("Sybase","org.apache.hop.databases.sybase.SybaseDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_SYBASEIQ, new Connection("Sybase IQ","org.apache.hop.databases.sybaseiq.SybaseIQDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_TERADATA, new Connection("Teradata","org.apache.hop.databases.teradata.TeradataDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_UNIVERSE, new Connection("UniVerse database","org.apache.hop.databases.universe.UniVerseDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_VERTICA, new Connection("Vertica","org.apache.hop.databases.vertica.VerticaDatabaseMeta"));
		connectionsMap.put(DATABASE_TYPE_VERTICA5, new Connection("Vertica 5","org.apache.hop.databases.vertica.Vertica5DatabaseMeta"));
		return connectionsMap;
	}
}
