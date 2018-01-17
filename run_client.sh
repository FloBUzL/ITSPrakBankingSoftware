#!/bin/bash
cd src
javac -d build -cp ../lib/* -sourcepath ./ client/main/ClientStart.java
cd ../build/
java client.main.ClientStart {$1} {$2}
cd ..