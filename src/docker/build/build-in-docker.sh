#!/usr/bin/env bash

echo "Removing old target folder..."
rm -rf target/

echo "Packaging..."
mvn package

echo "Adding HBPS1 certificate to key store..."
yes | keytool -import -trustcacerts -alias hbps1.chuv.ch -file /hbps1.crt -storepass 'changeit' -keystore $JAVA_HOME/jre/lib/security/cacerts

echo "Running Sonar analysis"
mvn sonar:sonar
