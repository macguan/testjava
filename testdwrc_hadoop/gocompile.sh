#!/bin/sh
javac -encoding UTF-8 -d ./bin -classpath .:./lib/*:./lib/apachecommons/*:./lib/db/*:./lib/hadoophive/* ./src/*.java
