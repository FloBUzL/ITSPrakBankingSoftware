#!/bin/bash
cd src
javac -d build -cp ../lib/* -sourcepath ./ server/main/ServerStart.java
cd ../build/
java server.main.ServerStart ../resources/database${1}.json 20180
cd ..