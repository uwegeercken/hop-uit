#!/bin/bash
#
# Script to run the Hop Universal Import Tool (hop-uit)
# The tool converts Pentaho PDI .ktr files into .hpl files for the project-hop platform.
#
# uwe.geercken@web.de - last update: 2020-05-18

lib_hop_uit=hop-uit-0.0.1-SNAPSHOT.jar

inputfolder=${1}
outputfolder=${2}
configfolder=${3}

java -cp .:lib/*:${lib_hop_uit} org.apache.tools.ImportTool -i="${inputfolder}" -o="${outputfolder}" -c="${configfolder}"
