#!/bin/sh

C4HOME=/var/parser
C4HOME=/Users/mircea/Workspace/git/c4curator/catalog-data-curator

if [ ! -f $C4HOME/bin/application.properties ]; then
    echo "linking to config file"
    ln -s $C4HOME/src/main/resources/application.properties $C4HOME/bin/application.properties
fi

echo "starting exploded app..."
cd $C4HOME
mvn clean package -DskipTests
# app cannot start bundled as one jar file. It contains /mock folder so we need to run it exploded
mvn spring-boot:run &

echo Done