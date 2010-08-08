#!/bin/bash

find . -name \*.java -exec svn ps svn:keywords "Revision Date Id" '{}' \;
find . -name \*.java -exec svn ps svn:eol-style native '{}' \;
find . -name \*.xsl  -exec svn ps svn:eol-style native '{}' \;
find . -name \*.xml  -exec svn ps svn:eol-style native '{}' \;
find . -name \*.properties  -exec svn ps svn:eol-style native '{}' \;
