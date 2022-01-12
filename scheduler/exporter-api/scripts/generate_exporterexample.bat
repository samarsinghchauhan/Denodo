@echo off
REM ---------------------------------------------------------------------------
REM  Environment variable JAVA_HOME must be set and exported
REM ---------------------------------------------------------------------------

SET DENODO_HOME=/Applications/DenodoPlatform8.0

SET SAMPLES_HOME=%DENODO_HOME%\samples\scheduler\exporter-api

SET JAR_BIN=%JAVA_HOME%\bin\jar.exe
if NOT exist "%JAR_BIN%" SET JAR_BIN=jar.exe

"%JAR_BIN%" cfm "%SAMPLES_HOME%\target\XMLCustomExporter.jar" "%SAMPLES_HOME%\conf\MANIFEST.MF" -C "%SAMPLES_HOME%"\target\classes com
"%JAR_BIN%" uf "%SAMPLES_HOME%\target\XMLCustomExporter.jar" -C "%SAMPLES_HOME%"\src com\denodo\scheduler\demo\XMLCustomExporter.xml