#!/bin/sh

echo "
### BEFORE"
jps | grep -vi jps
ps axwww | grep "java -jar" | grep -vi grep
echo

echo "shutting down..."
jps | grep curator | cut -d' ' -f1 | xargs kill

echo "
### AFTER"
jps | grep -vi jps
ps axwww | grep "java -jar" | grep -vi grep
echo "### processes listening on ports"
netstat -tnlp

echo "
Done
"