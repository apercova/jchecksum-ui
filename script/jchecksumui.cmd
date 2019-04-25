@echo off
if "%JAVA_HOME%"=="" (
set /p JAVA_HOME="JAVA_HOME: [%JAVA_HOME%]"
)
start /b %JAVA_HOME%\bin\javaw -jar %~dp0\jchecksum-ui-1.0.1904.jar %1 %2 %3 %4 %5 %6 %7 %8 %9
