@echo off
REM ---------------------------------------------------------------------------
REM  Environment variable JAVA_HOME must be set and exported
REM ---------------------------------------------------------------------------

SET DENODO_HOME=/Applications/DenodoPlatform8.0

SET SAMPLES_HOME=%DENODO_HOME%\samples\scheduler\handler-api

SET LIB=%DENODO_HOME%\lib

SET DENODOUTIL_JAR=%LIB%\contrib\denodo-commons-util.jar
SET DENODOCONFIGURATION_JAR=%LIB%\contrib\denodo-configuration.jar
SET SCHEDULERCLIENT_JAR=%LIB%\scheduler-client-core\denodo-scheduler-client.jar
SET SCHEDULERAPI_JAR=%LIB%\scheduler-client-core\denodo-scheduler-api.jar
SET DENODOCOMMONS_JAR=%LIB%\contrib\denodo-commons.jar
SET COMMONS_COLLECTION_JAR=%LIB%\contrib\commons-collections4.jar
SET LOG_JAR=%LIB%\contrib\log4j-api.jar;%LIB%\contrib\log4j-core.jar

SET JAVAC_BIN=%JAVA_HOME%\bin\javac.exe
if NOT exist "%JAVAC_BIN%" SET JAVAC_BIN=javac.exe

"%JAVAC_BIN%" -d "%SAMPLES_HOME%\target\classes" -classpath "%DENODOUTIL_JAR%;%DENODOCOMMONS_JAR%;%DENODOCONFIGURATION_JAR%;%SCHEDULERAPI_JAR%;%SCHEDULERCLIENT_JAR%;%COMMONS_COLLECTION_JAR%;%CLIENTCLASSPATH%;%LOG_JAR%" "%SAMPLES_HOME%\src\com\denodo\scheduler\demo\XMLCustomHandler.java"