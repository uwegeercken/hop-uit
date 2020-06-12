Hop Ultimate Import Tool

Program to convert .kjb and .ktr files created by the Pentaho Data Integration tool (PDI) into the equivalent HOP formats .hpl and .hwf.

Files are read from the input folder and recursively from subfolders, converted and output in the output folder. If additionally individual file names are specified, then only these files are processed from the input folder. The -f argument - to specify a filename - may be defined multiple times. If no file name is specified then all files in the input folder are processed. Only ktr and kjb files are processed. Files are not overwritten in case they already exist.

A folder "hop-uit-environment" is created  in the output folder and represents a Hop environment base folder. Database metadata files from the conversion are created in a subfolder of this folder. If a Hop config directory is defined as the system variable HOP_CONFIG_DIRECTORY or it is specified using the -c flag, then an environment metadata file is created in this location. This metadata file points to the Hop environment base folder and will allow the user to switch to this environment from the Hop GUI and have all converted database metadata readily available. Note that some environment related features are not yet implemented.

You may optionally specify a name for the environment metadata file that will be created.

NOTE: If no system variable HOP_CONFIG_DIRECTORY and no -c flag is specified, then the environment metadata file is created in the output folder specified. It can optionally be copied from this folder to the folder: /[Hop installation folder]/config/environments/metastore/Hop Environment, manually at a later point in time. Alternatively a new environment can be defined in the Hop GUI which points to the "hop-uit-environment" folder.

Adjust the log level in log4j2.properties file to DEBUG to receive more detailed output.

Build:

Run a "mvn clean install" to build the package. In the folder named "target" there will be

* a folder "lib" with all dependent libraries
* the hop-uit library file: hop-uit-<version>.jar
* a log4j2.properties file
* a shell script to run the tool.

NOTE: You will need to change the file permissions on the hop-uit.sh script so that the file is executable.

Usage:

./hop-uit.sh -i=[inputfolder] -o=[outputfolder] -f=[file name] -c=[config directory] -e=[environment file name]

* [inputfolder]           : required. path to the folder where the .ktr or .kjb files are located
* [outputfolder]          : required. path to the folder where the converted ktr files (hpl files) are output to
* [file name]             : optional. name of a ktr or kjb file to convert. argument may be specified multiple times
* [config directory]      : optional. path to the Hop config directory
* [environment file name] : optional. name of the environment file - with the xml extension

Examples:

* ./hop-uit.sh -i=/home/me/input -o=/home/me/output
* ./hop-uit.sh -i=/home/me/input -o=/home/me/output -c=/home/me/hop/config
* ./hop-uit.sh -i=/home/me/input -o=/home/me/output -f=myfile.ktr
* ./hop-uit.sh -i=/home/me/input -o=/home/me/output -f=myfile.ktr -e=myenv-001.xml
* ./hop-uit.sh -i=/home/me/input -o=/home/me/output -f=myfile1.ktr -f=myfile2.ktr

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

last update: 2020-06-03
