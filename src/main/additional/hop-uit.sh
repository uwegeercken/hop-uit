#!/bin/bash
#
# Script to run the Hop Ultimate Import Tool (hop-uit)
# The tool converts Pentaho PDI .ktr and .kjb files into .hpl and .hwf files for the project-hop platform.
#
# uwe.geercken@web.de - last update: 2020-06-30

java -cp .:lib/* com.datamelt.hop.uit.ImportTool "$@"
