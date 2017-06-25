#!/usr/bin/env bash


# this script will build, deploy and start app in a docker container
#
# docker doc https://spring.io/guides/gs/spring-boot-docker/
# docker repo https://hub.docker.com/r/mirceahalmagean/c4/
# https://docs.docker.com/get-started/part2/
#
# Note
# - TODO see pom.xml <docker.image.prefix>mirceahalmagean</docker.image.prefix> A proper public c4 image registry should be used
#

#

# goto project home folder
cd ../

echo "
###
### build docker assembly folder - see target/docker
###
"
mvn clean package docker:build -DskipTests

cd target/docker

echo "
###
### create docker image with name catalog-politic
###
"
docker build -t catalog-politic .

echo "
###
### run catalog-politic image on 8080:8080 port mapping between docker OS and local OS
###
"
docker run -p 8080:8080 catalog-politic -name catalog-politic &

echo "
###
### You can access app at http://localhost.ingenuity.com:8080/c4curator/
### To stop it run 'docker ps' and with image id run 'docker stop image-id'
###
"
