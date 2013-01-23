#!/bin/bash
#
# Create release-file to be downloaded
#
######################################
VERSION=1.0.0

##
## Set home-directory if not set
##

if [ "x$AS_HOME" = "x" ]; then
    AS_HOME=$(cd $(dirname "$0"); pwd)
fi

RELEASE_FOLDER=$AS_HOME/release-$VERSION

##
## Make release-folder and subfolder if not exists
##

mkdir -p $RELEASE_FOLDER
mkdir -p $RELEASE_FOLDER/bin
mkdir -p $RELEASE_FOLDER/lib
mkdir -p $RELEASE_FOLDER/target
mkdir -p $RELEASE_FOLDER/conf
mkdir -p $RELEASE_FOLDER/logs

##
## Copy content into folders
##

cp $AS_HOME/LICENSE.txt $RELEASE_FOLDER/LICENSE.txt
CP $AS_HOME/start_test.sh $RELEASE_FOLDER/start_test.sh
cp -r $AS_HOME/bin/* $RELEASE_FOLDER/bin/
cp -r $AS_HOME/lib/* $RELEASE_FOLDER/lib/
cp -r $AS_HOME/conf/* $RELEASE_FOLDER/conf/
cp -r $AS_HOME/target/autoscale-agent-$VERSION.jar $RELEASE_FOLDER/target/autoscale-agent-$VERSION.jar

##
## Create tarball file for release
##

tar czf $AS_HOME/release-$VERSION.tar.gz release-$VERSION