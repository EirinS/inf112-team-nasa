#!/bin/bash

for file in $(ls); do
	first=$(cut -f 0 -d " " $file)
	second=$(cut -f 1 -d " " $file)
        mv $file "$first$second"
done
