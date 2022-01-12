#!/bin/bash
# -----------------------------------------------------------------------------
#  Environment variable JAVA_HOME must be set and exported
# -----------------------------------------------------------------------------

DENODO_HOME="/Applications/DenodoPlatform8.0"

SAMPLES_HOME=$DENODO_HOME/samples/vdp/customFunctions

DENODOCUSTOM_JAR=$DENODO_HOME/lib/contrib/denodo-commons-custom.jar

SRC_DIR=$DENODO_HOME/samples/vdp/customFunctions/src

if [ -a "$JAVA_HOME/bin/javac" ]
then
    JAVAC_BIN="$JAVA_HOME/bin/javac"
else
    JAVAC_BIN=javac
fi

if [ -a "$JAVA_HOME/bin/jar" ]
then
    JAR_BIN="$JAVA_HOME/bin/jar"
else
    JAR_BIN=jar
fi

$JAVAC_BIN -d "$SAMPLES_HOME/target/classes" -classpath \
    "$DENODOCUSTOM_JAR" \
    "$SRC_DIR/com/denodo/vdp/demo/function/custom/Concat_SampleVdpFunction.java" \
    "$SRC_DIR/com/denodo/vdp/demo/function/custom/Group_Concat_SampleVdpAggregateFunction.java" \
    "$SRC_DIR/com/denodo/vdp/demo/function/custom/Split_SampleVdpFunction.java"

mkdir -p "$SAMPLES_HOME/target/jars"

$JAR_BIN cf "$SAMPLES_HOME/target/jars/denodo-demo-customfunctions.jar" -C "$SAMPLES_HOME/target/classes" .