Hop Ultimate Import Tool

Program to convert files created by the Pentaho Data Integration tool (PDI) into the HOP format.

Files are read from the input folder, converted and output to the output folder. Database metadata files are created in a subfolder of the Hop config directory folder. If a file name is specified, this file	is processed from the input folder. The -f argument may be used multiple times. If no file name is specified then all files in the input folder are processed. Files are not overwritten in case they already exist.

Build:

Run a "mvn clean install" to build the package. In the folder named "target" there will be

* a folder "lib" with all dependent libraries
* the hop-uit library file: hop-uit-<version>.jar
* a shell script to run the tool.

NOTE: You will need to change the file permissions on the hop-uit.sh script so that the file is executable.

Usage:

ImportTool -i=[inputfolder] -o=[outputfolder] -f=[file name] -c=[Hop config directory]
where

* [inputfolder]          : required. path to the folder where the ktr files are located
* [outputfolder]         : required. path to the folder where the converted ktr files (hpl files) are output to
* [file name]            : optional. name of a single ktr file to convert
* [Hop config directory] : required. name of the folder where the Hop metadata files are stored

Examples:

* ImportTool -i=/home/me/input -o=/home/me/output -c=/home/me/config
* ImportTool -i=/home/me/input -o=/home/me/output -f=myfile.ktr -c=/home/me/config
* ImportTool -i=/home/me/input -o=/home/me/output -f=myfile1.ktr -f=myfile2.ktr -c=/home/me/config

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

last update: 2020-05-20
