#!/bin/bash

# Call this from with in fonts/directory and send the output to MANIFEST.MF

echo 
ls | awk '{print "Name: fonts/"$1;print "Content-Type: application/x-font"; print "";} '

