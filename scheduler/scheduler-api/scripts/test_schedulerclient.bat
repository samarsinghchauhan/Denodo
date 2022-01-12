@echo off
REM ---------------------------------------------------------------------------
REM  Environment variable JAVA_HOME must be set and exported
REM ---------------------------------------------------------------------------

SET DENODO_HOME=/Applications/DenodoPlatform8.0

SET SAMPLES_HOME=%DENODO_HOME%/samples/scheduler/scheduler-api

SET SAMPLES_CONF=%SAMPLES_HOME%/conf
SET SAMPLES_SRC=%SAMPLES_HOME%/src
SET SAMPLES_CLASSES=%SAMPLES_HOME%/target/classes

SET LIB=%DENODO_HOME%/lib

SET DENODOLAUNCHER_JAR=%LIB%/denodo-commons-launcher-util.jar
SET COMMONSCOLLECTIONS_JAR=%LIB%/contrib/commons-collections4.jar
SET COMMONSPOOL_JAR=%LIB%/contrib/commons-pool.jar
SET DENODOUTIL_JAR=%LIB%/contrib/denodo-commons-util.jar
SET COMMONSLANG_JAR=%LIB%/contrib/commons-lang3.jar
SET COMMONSLOGGING_JAR=%LIB%/contrib/commons-logging.jar
SET LOG4J_JAR=%LIB%/contrib/log4j-api.jar;%LIB%/contrib/log4j-core.jar
SET DENODOCONFIGURATION_JAR=%LIB%/contrib/denodo-configuration.jar
SET SCHEDULERAPI_JAR=%LIB%/scheduler-client-core/denodo-scheduler-api.jar
SET SCHEDULERCLIENT_JAR=%LIB%/scheduler-client-core/denodo-scheduler-client.jar
SET DENODOCOMMONS_JAR=%LIB%/contrib/denodo-commons.jar

SET JAVA_BIN=%JAVA_HOME%/jre/bin/java.exe
if NOT exist "%JAVA_BIN%" SET JAVA_BIN=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_BIN%" (
    "%JAVA_BIN%" -DverboseMode=false -classpath "%DENODOLAUNCHER_JAR%;%DENODOUTIL_JAR%;%COMMONSCOLLECTIONS_JAR%;%COMMONSPOOL_JAR%;%COMMONSLANG_JAR%;%COMMONSLOGGING_JAR%;%LOG4J_JAR%;%DENODOCONFIGURATION_JAR%;%SCHEDULERAPI_JAR%;%SCHEDULERCLIENT_JAR%;%DENODOCOMMONS_JAR%" -Djava.system.class.loader=com.denodo.util.launcher.DenodoClassLoader com.denodo.util.launcher.Launcher com.denodo.scheduler.demo.SchedulerJobsConfigurationSample --lib "%LIB%/scheduler-client-core" --class "%SAMPLES_CLASSES%" --conf "%SAMPLES_CONF%" --arg %*
    goto end
)
echo "Unable to execute %0: Environment variable JAVA_HOME must be set"
:end
