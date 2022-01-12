#!/bin/bash

# -----------------------------------------------------------------------------
#  Environment variable JAVA_HOME must be set and exported
# -----------------------------------------------------------------------------

DENODO_HOME="/Applications/DenodoPlatform8.0"

SAMPLES_HOME=$DENODO_HOME/samples/scheduler/scheduler-api

LIB=$DENODO_HOME/lib

CLIENTCLASSPATH=$LIB/contrib/denodo-commons-stream.jar
DENODOUTIL_JAR=$LIB/contrib/denodo-commons-util.jar
COMMONSLANG_JAR=$LIB/contrib/commons-lang3.jar
COMMONSLOGGING_JAR=$LIB/contrib/commons-logging.jar
LOG4J_JAR=$LIB/contrib/log4j-api.jar:$LIB/contrib/log4j-core.jar
DENODOCONFIGURATION_JAR=$LIB/contrib/denodo-configuration.jar
SCHEDULERAPI_JAR=$LIB/scheduler-client-core/denodo-scheduler-api.jar
SCHEDULERCLIENT_JAR=$LIB/scheduler-client-core/denodo-scheduler-client.jar
DENODOCOMMONS_JAR=$LIB/contrib/denodo-commons.jar


if [ -a "$JAVA_HOME/bin/javac" ]
then
    JAVAC_BIN="$JAVA_HOME/bin/javac"
else
    JAVAC_BIN=javac
fi

find . "$SAMPLES_HOME/src/" -name *.java > sources.txt

sed 's/^.*$/"&"/' sources.txt >> compile.txt

$JAVAC_BIN -d "$SAMPLES_HOME/target/classes" \
    -classpath  "$DENODOUTIL_JAR:$DENODOCOMMONS_JAR:$DENODOCONFIGURATION_JAR:$SCHEDULERAPI_JAR:$SCHEDULERCLIENT_JAR:$COMMONSLANG_JAR:$COMMONSLOGGING_JAR:$LOG4J_JAR:$CLIENTCLASSPATH" \
    @compile.txt

rm sources.txt
rm compile.txt