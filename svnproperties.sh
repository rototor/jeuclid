#!/bin/bash

find . -name \*.java -exec svn ps svn:keywords "Revision Date Id" '{}' \;
find . -name \*.java -exec svn ps svn:eol-style native '{}' \;
