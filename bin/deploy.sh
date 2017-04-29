#!/usr/bin/env bash

BUILD_NAME=c4curator-new.tgz

cd ../
rm -r target
rm -r logs/*
tar -czvf ./bin/$BUILD_NAME *
cd bin/

echo "copy new build"
scp $BUILD_NAME develop@207.154.213.120:./

echo "running deploy script"
ssh develop@207.154.213.120 "/var/parser/bin/replace.build.sh ~/$BUILD_NAME"

echo Done