#!/bin/sh

# Build .jar
./gradlew clean assemble

# Install
if [ ! -d "~/.android/lint/" ]; then
  mkdir ~/.android/lint/
fi

rm ~/.android/lint/OlaChecks-v2.jar
cp build/libs/* ~/.android/lint/
