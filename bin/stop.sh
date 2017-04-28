#!/bin/sh

jps | grep curator | cut -d' ' -f1 | xargs kill