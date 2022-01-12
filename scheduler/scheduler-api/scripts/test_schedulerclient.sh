#!/bin/bash

# -----------------------------------------------------------------------------
#  Environment variable JAVA_HOME must be set and exported
# -----------------------------------------------------------------------------

DENODO_HOME="/Applications/DenodoPlatform8.0"

SAMPLES_HOME=$DENODO_HOME/samples/scheduler/scheduler-api

SAMPLES_CONF=$SAMPLES_HOME/conf
SAMPLES_SRC=$SAMPLES_HOME/src
SAMPLES_CLASSES=$SAMPLES_HOME/target/classes

LIB=$DENODO_HOME/lib

DENODOLAUNCHER_JAR=$LIB/denodo-commons-launcher-util.jar
COMMONSCOLLECTIONS_JAR=$LIB/contrib/commons-collections4.jar
COMMONSPOOL_JAR=$LIB/contrib/commons-pool.jar
DENODOUTIL_JAR=$LIB/contrib/denodo-commons-util.jar
COMMONSLANG_JAR=$LIB/contrib/commons-lang3.jar
COMMONSLOGGING_JAR=$LIB/contrib/commons-logging.jar
LOG4J_JAR=$LIB/contrib/log4j-api.jar:$LIB/contrib/log4j-core.jar
DENODOCONFIGURATION_JAR=$LIB/contrib/denodo-configuration.jar
SCHEDULERAPI_JAR=$LIB/scheduler-client-core/denodo-scheduler-api.jar
SCHEDULERCLIENT_JAR=$LIB/scheduler-client-core/denodo-scheduler-client.jar
DENODOCOMMONS_JAR=$LIB/contrib/denodo-commons.jar

if [ -a "$JAVA_HOME/bin/java" ]
then
    JAVA_BIN="$JAVA_HOME/bin/java"
else
    JAVA_BIN=java
fi

$JAVA_BIN -DverboseMode=false -Djava.system.class.loader=com.denodo.util.launcher.DenodoClassLoader -classpath "$DENODOLAUNCHER_JAR:$DENODOUTIL_JAR:$COMMONSCOLLECTIONS_JAR:$COMMONSPOOL_JAR:$COMMONSLANG_JAR:$COMMONSLOGGING_JAR:$LOG4J_JAR:$DENODOCONFIGURATION_JAR:$SCHEDULERAPI_JAR:$SCHEDULERCLIENT_JAR:$DENODOCOMMONS_JAR" com.denodo.util.launcher.Launcher com.denodo.scheduler.demo.SchedulerJobsConfigurationSample --lib "$LIB/scheduler-client-core" --class "$SAMPLES_CLASSES" --conf "$SAMPLES_CONF" --arg $1 --arg "$2" --arg $3 --arg $4 --arg $5 --arg $6 --arg "$7" --arg $8