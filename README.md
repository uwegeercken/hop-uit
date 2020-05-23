Hop Ultimate Import Tool

Program to convert .kjb and .ktr files created by the Pentaho Data Integration tool (PDI) into the equivalent HOP formats .hpl and .hwf.

Files are read from the input folder, converted and output to the output folder. If one or multiple file names are specified, then only these files are processed from the input folder. The -f argument to specify a filename may be defined multiple times. If no file name is specified then all files in the input folder are processed. Files are not overwritten in case they already exist.

A folder "hop-uit-environment" is created as a Hop environment base folder. Database metadata files are created in a subfolder of this folder. After running the hop-uit tool and after the conversion of input files is complete, one can define a new environment in the Hop GUI poiting the value of "Environment base folder" to the "hop-uit-environment" folder that was created runnint the hop-uit tool. This way all database connection files that were converted and created are readily available in Hop.

Adjust the log level in log4j.properties to DEBUG to receive more detailed output.

Build:

Run a "mvn clean install" to build the package. In the folder named "target" there will be

* a folder "lib" with all dependent libraries
* the hop-uit library file: hop-uit-<version>.jar
* a log4j.properties file
* a shell script to run the tool.

NOTE: You will need to change the file permissions on the hop-uit.sh script so that the file is executable.

Usage:

ImportTool -i=[inputfolder] -o=[outputfolder] -f=[file name]
where

* [inputfolder]          : required. path to the folder where the ktr files are located
* [outputfolder]         : required. path to the folder where the converted ktr files (hpl files) are output to
* [file name]            : optional. name of a ktr file to convert. argument may be specified multiple times.

Examples:

* ImportTool -i=/home/me/input -o=/home/me/output
* ImportTool -i=/home/me/input -o=/home/me/output -f=myfile.ktr
* ImportTool -i=/home/me/input -o=/home/me/output -f=myfile1.ktr -f=myfile2.ktr

Please send your feedback and help to enhance the tool.

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

last update: 2020-05-23
