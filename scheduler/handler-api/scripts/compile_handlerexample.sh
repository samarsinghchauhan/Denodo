#!/bin/bash
# -----------------------------------------------------------------------------
#  Environment variable JAVA_HOME must be set and exported
# -----------------------------------------------------------------------------

DENODO_HOME="/Applications/DenodoPlatform8.0"

SAMPLES_HOME=$DENODO_HOME/samples/scheduler/handler-api

LIB=$DENODO_HOME/lib

DENODOUTIL_JAR=$LIB/contrib/denodo-commons-util.jar
DENODOCONFIGURATION_JAR=$LIB/contrib/denodo-configuration.jar
SCHEDULERCLIENT_JAR=$LIB/scheduler-client-core/denodo-scheduler-client.jar
SCHEDULERAPI_JAR=$LIB/scheduler-client-core/denodo-scheduler-api.jar
DENODOCOMMONS_JAR=$LIB/contrib/denodo-commons.jar
COMMONS_COLLECTION_JAR=$LIB/contrib/commons-collections4.jar
LOG_JAR=$LIB/contrib/log4j-api.jar;$LIB/contrib/log4j-core.jar

if [ -a "$JAVA_HOME/bin/javac" ]
then
    JAVAC_BIN="$JAVA_HOME/bin/javac"
else
    JAVAC_BIN=javac
fi

$JAVAC_BIN -d "$SAMPLES_HOME/target/classes" -classpath "$DENODOUTIL_JAR:$DENODOCOMMONS_JAR:$DENODOCONFIGURATION_JAR:$SCHEDULERAPI_JAR:$SCHEDULERCLIENT_JAR:$COMMONS_COLLECTION_JAR:$LOG_JAR" "$SAMPLES_HOME/src/com/denodo/scheduler/demo/XMLCustomHandler.java"
