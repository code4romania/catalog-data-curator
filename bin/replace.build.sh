#!/usr/bin/env bash

if [ $# -eq 0 ]
  then
    echo "No arguments supplied"
    echo "Usage: replace.build.sh /path/to/app.bundle.tgz"
    exit 1
fi

if [ ! -f $1 ]; then
    echo "File not found ! $1"
    exit 1
fi

TIMESTAMP=`date "+%Y-%m-%d-%H-%M-%S"`
PARSER_HOME=/var/parser
OLD_BUNDLE_NAME=c4curator_$TIMESTAMP.tgz
NEW_BUNDLE_NAME=$1


echo "stop app and clean build artifacts"
$PARSER_HOME/bin/stop.sh
rm -r $PARSER_HOME/logs
rm -r $PARSER_HOME/bin/logs
rm -r ~/logs
cd $PARSER_HOME
mvn clean

echo "archive existing sources"
mv $PARSER_HOME/bin/c4*.tgz /tmp/
tar -czvf $PARSER_HOME/$OLD_BUNDLE_NAME *
mv $PARSER_HOME/$OLD_BUNDLE_NAME /tmp/
ls /tmp/c4*

echo "unpacking new build"
tar -xzvf $1

echo "moving builds to bin/"
mv $1 $PARSER_HOME/bin/
mv /tmp/c4*.tgz $PARSER_HOME/bin/

echo Done