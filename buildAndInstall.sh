#!/bin/sh
# Builds a jar for the lint checks and installs it to the lint directory so that it runs for all Android projects.

# Build .jar
./gradlew clean assemble

# Install
if [ ! -d "~/.android/lint/" ]; then
  mkdir ~/.android/lint/
fi

rm ~/.android/lint/OlaChecks-v2.jar
cp build/libs/* ~/.android/lint/
