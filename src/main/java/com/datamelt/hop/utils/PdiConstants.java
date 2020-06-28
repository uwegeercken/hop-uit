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

/**
 * Constants related to Pentaoho PDI
 * 
 * 
 * @author uwe geercken - uwe.geercken@web.de
 *
 */
public class PdiConstants 
{
	public static final String HOP_PIPELINE_TAG_TRANSFORM = "transform";
	public static final String HOP_WORKFLOW_TAG_ACTION = "action";
	
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
}
