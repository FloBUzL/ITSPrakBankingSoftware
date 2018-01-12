#!/bin/bash
javac -d bin -cp lib/*:. src/ClientThread.java src/Database.java src/ServerMain.java src/Tuple.java src/UserData.java src/Utility.java
cd bin
java -cp ../lib/*:. ServerMain database${1}.json 20180
cd ..
