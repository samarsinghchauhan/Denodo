@echo off
REM ---------------------------------------------------------------------------
REM  Environment variable JAVA_HOME must be set and exported
REM ---------------------------------------------------------------------------

SET DENODO_HOME=/Applications/DenodoPlatform8.0

SET SAMPLES_HOME=%DENODO_HOME%/samples/vdp/customFunctions

SET DENODOCUSTOM_JAR=%DENODO_HOME%/lib/contrib/denodo-commons-custom.jar

SET SRC_DIR=%DENODO_HOME%/samples/vdp/customFunctions/src

SET JAVAC_BIN=%JAVA_HOME%\bin\javac.exe
if NOT exist "%JAVAC_BIN%" SET JAVAC_BIN=javac.exe

SET JAR_BIN=%JAVA_HOME%\bin\jar.exe
if NOT exist "%JAR_BIN%" SET JAR_BIN=jar.exe

"%JAVAC_BIN%" -d "%SAMPLES_HOME%/target/classes" -classpath "%DENODOCUSTOM_JAR%;" "%SRC_DIR%/com/denodo/vdp/demo/function/custom/Concat_SampleVdpFunction.java" "%SRC_DIR%/com/denodo/vdp/demo/function/custom/Group_Concat_SampleVdpAggregateFunction.java" "%SRC_DIR%/com/denodo/vdp/demo/function/custom/Split_SampleVdpFunction.java"

if NOT exist "%SAMPLES_HOME%/target/jars" (
	mkdir "%SAMPLES_HOME%/target/jars"
)
"%JAR_BIN%" cf "%SAMPLES_HOME%/target/jars/denodo-demo-customfunctions.jar" -C "%SAMPLES_HOME%/target/classes" .