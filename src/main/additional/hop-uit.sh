#!/bin/bash
#
# Script to run the Hop Ultimate Import Tool (hop-uit)
# The tool converts Pentaho PDI .ktr and .kjb files into .hpl and .hwf files for the project-hop platform.
#
# uwe.geercken@web.de - last update: 2020-05-23

lib_hop_uit=hop-uit-0.1.4-SNAPSHOT.jar

java -cp .:lib/*:${lib_hop_uit} com.datamelt.hop.uit.ImportTool "$@"
