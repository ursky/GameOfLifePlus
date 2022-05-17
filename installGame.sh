#!/usr/bin/env bash
# This is a simple macro to compile the game java code and build a jar executable.

echo "Building Java classes..."
javac -d bin -sourcepath src src/LaunchGame.java

echo "Generating .jar file..."
cd  bin/
jar -cmf ../MANIFEST.MF runGame.jar .
mv runGame.jar ../
cd ../

