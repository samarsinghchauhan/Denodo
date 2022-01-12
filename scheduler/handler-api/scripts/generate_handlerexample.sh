#!/bin/bash
# -----------------------------------------------------------------------------
#  Environment variable JAVA_HOME must be set and exported
# -----------------------------------------------------------------------------

DENODO_HOME="/Applications/DenodoPlatform8.0"

SAMPLES_HOME=$DENODO_HOME/samples/scheduler/handler-api

if [ -a "$JAVA_HOME/bin/jar" ]
then
    JAR_BIN="$JAVA_HOME/bin/jar"
else
    JAR_BIN=jar
fi

cd "$SAMPLES_HOME/target/classes/"
$JAR_BIN cfm "$SAMPLES_HOME/target/XMLCustomHandler.jar" "$SAMPLES_HOME/conf/MANIFEST.MF" com
cd "$SAMPLES_HOME/src/"
$JAR_BIN uf "$SAMPLES_HOME/target/XMLCustomHandler.jar" com/denodo/scheduler/demo/XMLCustomHandler.xml
cd "$SAMPLES_HOME/scripts"
