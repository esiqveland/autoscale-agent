#!/bin/sh
#
# Create the standalone solution for the autoscale-slave
# implementation.
#
# Puts lib/*, bin/*, conf/*, target/autoscale-slave-1.0.0.jar
# into a single release tar.gz archive.
VERSION=1.0.0
FULLNAME=autoscale-slave-$VERSION


## If directory exists. Remove content
if [ -d release/ ];
then
	rm -R release/
fi

## Create folder-structure
mkdir release
mkdir release/$FULLNAME
mkdir release/$FULLNAME/target

## Copy content
cp -R bin/ release/$FULLNAME/bin
cp -R lib/ release/$FULLNAME/lib
cp -R conf/ release/$FULLNAME/conf
cp -R target/$FULLNAME.jar release/$FULLNAME/target/

## Create archive
cd release/
tar -zcvf $FULLNAME.tar.gz $FULLNAME
cd ..

## Remove temp-directory
rm -R release/$FULLNAME