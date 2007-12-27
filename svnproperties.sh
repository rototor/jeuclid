#!/bin/bash

find . -name \*.java -exec svn ps svn:keywords "Revision Date Id" '{}' \;
find . -name \*.java -or -name \*.xsl -or -name \*.xml -exec svn ps svn:eol-style native '{}' \;
