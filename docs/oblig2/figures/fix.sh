#!/bin/bash

for file in `ls`; do
	first=`cut -f 1 -d " " $file`
	second=`cut -f 2 -d " " $file`
        mv $file "$first$second"
done
