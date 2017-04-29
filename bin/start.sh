#!/bin/sh

if [ ! -f application.properties ]; then
    echo "linking to config file"
    ln -s /var/parser/src/main/resources/application.properties /var/parser/bin/application.properties
fi

if [ ! -f /var/parser/target/c4-curator-0.1.0.jar ]; then
    echo "missing app file, will need to rebuild"
    /var/parser/bin/build.sh
fi

echo "starting app..."
java -jar /var/parser/target/c4-curator-0.1.0.jar &

echo Done