#!/bin/bash
#
# Script to run the Hop Ultimate Import Tool (hop-uit)
# The tool converts Pentaho PDI .ktr and .kjb files into .hpl and .hwf files for the project-hop platform.
#
# uwe.geercken@web.de - last update: 2020-05-23

lib_hop_uit=hop-uit-0.1.3-SNAPSHOT.jar

if [ "$#" -ne 2 ]; 
then
	java -cp .:lib/*:${lib_hop_uit} com.datamelt.hop.uit.ImportTool -h	
else
	inputfolder=${1}
	outputfolder=${2}
	
	java -cp .:lib/*:${lib_hop_uit} com.datamelt.hop.uit.ImportTool -i="${inputfolder}" -o="${outputfolder}"
fi

