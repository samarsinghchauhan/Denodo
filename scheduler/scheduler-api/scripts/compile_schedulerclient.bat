@echo off
REM ---------------------------------------------------------------------------
REM  Environment variable JAVA_HOME must be set and exported
REM ---------------------------------------------------------------------------

SET DENODO_HOME=/Applications/DenodoPlatform8.0

SET SAMPLES_HOME=%DENODO_HOME%/samples/scheduler/scheduler-api
SET SAMPLES_HOME_CD="%SAMPLES_HOME:/=\%"

SET CURRENT_HOME=%cd%

SET LIB=%DENODO_HOME%/lib

SET CLIENTCLASSPATH=%LIB%/contrib/denodo-commons-stream.jar
SET DENODOUTIL_JAR=%LIB%/contrib/denodo-commons-util.jar
SET COMMONSLANG_JAR=%LIB%/contrib/commons-lang3.jar
SET COMMONSLOGGING_JAR=%LIB%/contrib/commons-logging.jar
SET LOG4J_JAR=%LIB%/contrib/log4j-api.jar;%LIB%/contrib/log4j-core.jar
SET DENODOCONFIGURATION_JAR=%LIB%/contrib/denodo-configuration.jar
SET SCHEDULERAPI_JAR=%LIB%/scheduler-client-core/denodo-scheduler-api.jar
SET SCHEDULERCLIENT_JAR=%LIB%/scheduler-client-core/denodo-scheduler-client.jar
SET DENODOCOMMONS_JAR=%LIB%/contrib/denodo-commons.jar


SET JAVAC_BIN=%JAVA_HOME%\bin\javac.exe
if NOT exist "%JAVAC_BIN%" SET JAVAC_BIN=javac.exe

cd "%SAMPLES_HOME_CD%"
"%JAVAC_BIN%" -d "%SAMPLES_HOME%/target/classes" -classpath "%DENODOUTIL_JAR%;%DENODOCOMMONS_JAR%;%DENODOCONFIGURATION_JAR%;%SCHEDULERAPI_JAR%;%SCHEDULERCLIENT_JAR%;%COMMONSLANG_JAR%;%COMMONSLOGGING_JAR%;%LOG4J_JAR%;%CLIENTCLASSPATH%" src/com/denodo/scheduler/demo/*.java

cd "%CURRENT_HOME%"
