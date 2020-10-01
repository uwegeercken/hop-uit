echo off
setlocal
set LIBSPATH=lib

:NormalStart
REM set java primary is HOP_JAVA_HOME fallback to JAVA_HOME or default java
if not "%HOP_JAVA_HOME%"=="" (
    set _HOP_JAVA="%HOP_JAVA_HOME%bin\java"
) else if not "%JAVA_HOME%"=="" (
    set _HOP_JAVA="%JAVA_HOME%bin\java"
) else (
    set _HOP_JAVA=java
)

REM # Settings for all OSses

if "%HOP_OPTIONS%"=="" set HOP_OPTIONS=-Xmx64m

echo ===[Environment Settings - hop-uit.bat]====================================
echo Hop Ultimate Import Tool
echo.
echo Java identified as %_HOP_JAVA%
echo.
echo HOP_OPTIONS=%HOP_OPTIONS%
echo.
echo.
rem ===[Collect command line arguments...]======================================
set _cmdline=
:TopArg
if %1!==! goto EndArg
set _cmdline=%_cmdline% %1
shift
goto TopArg
:EndArg

echo Command to start Hop will be:
echo %_HOP_JAVA% -classpath %LIBSPATH%\*;%SWTJAR%\* -Djava.library.path=%LIBSPATH% %HOP_OPTIONS% com.datamelt.hop.uit.ImportTool %_cmdline%
echo.
echo ===[Starting Hop Ultimate Import Tool]=========================================================

%_HOP_JAVA% -classpath %LIBSPATH%\*;\* -Djava.library.path=%LIBSPATH% %HOP_OPTIONS% com.datamelt.hop.uit.ImportTool %_cmdline%
@echo off
:End