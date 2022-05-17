#!/usr/bin/env bash

echo "Building Java classes..."
javac -d bin -sourcepath src src/LaunchGame.java

echo "Generating .jar file..."
cd  bin/
jar -cmf ../MANIFEST.MF runGame.jar .
mv runGame.jar ../
cd ../

