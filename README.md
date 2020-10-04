Hop Ultimate Import Tool

Program to convert .kjb and .ktr files created by the Pentaho Data Integration tool (PDI) into the equivalent HOP formats .hpl and .hwf. See the project-hop website for details: https://www.project-hop.org

Files are read from the input folder and recursively from subfolders, converted and output in the output folder. If additionally individual file names are specified, then only these files are processed from the input folder. The -f argument - to specify a filename - may be defined multiple times. If no file name is specified then all files in the input folder are processed. Only .ktr and .kjb files are processed. Files are not overwritten in case they already exist.

Per default the tool creates one project for each subfolder in the specified inputfolder. The project name corresponds to the name of the folder. Each of these project folders contains the converted files and a metadata folder with all database connections that are relevant to the converted files of the project. Files that are not located in any folder - in the inputfolder directly - are output to a default project folder. If the argument [project per subfolder] is set to false, then all converted files and database connections are output to a default project folder. In this case, all converted files go to the same output folder so you need to take care that you do not have files with the same name.

Adjust the log level in log4j2.properties file to DEBUG to receive more detailed output.

Build:

Run a "mvn clean install" to build the package. In the folder named "target" there will be a subfolder named "hop-uit" containing:

* a folder "lib" with all dependent libraries , also the hop-uit library file: hop-uit-<version>.jar
* a log4j2.properties file
* a shell script to run the tool on Linux.
* a bat file to run the tool in Windows.

Usage:

./hop-uit.sh -i=[inputfolder] -o=[outputfolder] -f=[file name] -s=[project per subfolder]

* [inputfolder]           : required. path to the folder where the .ktr or .kjb files are located
* [outputfolder]          : required. path to the folder where the converted ktr files (hpl files) are output to
* [file name]             : optional. name of a ktr or kjb file to convert. argument may be specified multiple times
* [project per subfolder] : optional. default=true. true or false, if a project per subfolder (located in the inputfolder) is created

Examples:

* ./hop-uit.sh -i=/home/me/input -o=/home/me/output
* ./hop-uit.sh -i=/home/me/input -o=/home/me/output -s=false
* ./hop-uit.sh -i=/home/me/input -o=/home/me/output -f=myfile.ktr
* ./hop-uit.sh -i=/home/me/input -o=/home/me/output -f=myfile1.ktr -f=myfile2.ktr -f=myfile3.kjb

Issues:

The multiway join step allows for any number of input steps - this tool only converts up to 6. If you have more than that, then either the code here has to be ammended or the step has to be removed
from the Hop GUI canvas and re-added. Note that the multiway join step is introduced in Hop v0.40.

Please send your feedback and help to enhance the tool. It is important to have your view of the world, your manpower and your expertise. 

Copyright (C) 2020  Uwe Geercken

Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

uwe geercken
uwe.geercken@web.de

last update: 2020-10-04
