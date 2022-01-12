@echo off
REM ---------------------------------------------------------------------------
REM  Environment variable JAVA_HOME must be set and exported
REM ---------------------------------------------------------------------------

SET DENODO_HOME=/Applications/DenodoPlatform8.0

SET SAMPLES_HOME=%DENODO_HOME%\samples\scheduler\exporter-api

SET LIB=%DENODO_HOME%\lib

SET DENODOUTIL_JAR=%LIB%\contrib\denodo-commons-util.jar
SET COMMONSCODEC_JAR=%LIB%\contrib\commons-codec.jar
SET DENODOCONFIGURATION_JAR=%LIB%\contrib\denodo-configuration.jar
SET SCHEDULERCLIENT_JAR=%LIB%\scheduler-client-core\denodo-scheduler-client.jar
SET SCHEDULERAPI_JAR=%LIB%\scheduler-client-core\denodo-scheduler-api.jar
SET DENODOCOMMONS_JAR=%LIB%\contrib\denodo-commons.jar

SET JAVAC_BIN=%JAVA_HOME%\bin\javac.exe
if NOT exist "%JAVAC_BIN%" SET JAVAC_BIN=javac.exe

"%JAVAC_BIN%" -d "%SAMPLES_HOME%\target\classes" -classpath "%DENODOUTIL_JAR%;%DENODOCOMMONS_JAR%;%DENODOCONFIGURATION_JAR%;%SCHEDULERAPI_JAR%;%SCHEDULERCLIENT_JAR%;%COMMONSCODEC_JAR%;%CLIENTCLASSPATH%" "%SAMPLES_HOME%\src\com\denodo\scheduler\demo\XMLCustomExporter.java"