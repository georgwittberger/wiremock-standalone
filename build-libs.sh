#!/bin/bash

SCRIPTFILE=$(readlink -f "$0")
BASEPATH=$(dirname "$SCRIPTFILE")

# Download required JAR files using Maven

cd "$BASEPATH/libs"
mvn -B dependency:copy
cd "$BASEPATH"

# Build extensions JAR file using Maven

cd "$BASEPATH/extensions"
mvn -B clean package
cd "$BASEPATH"
mv "$BASEPATH/extensions/target/wiremock-standalone-extensions.jar" "$BASEPATH/libs/wiremock-standalone-extensions.jar"
