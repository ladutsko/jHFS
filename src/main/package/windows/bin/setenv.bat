@echo off

:: JHFS_HOME
set JHFS_HOME=%~dp0..

:: JAVA_HOME
::set JAVA_HOME=

:: CLASSPATH
set CLASSPATH=%JHFS_HOME%\config;%JHFS_HOME%\lib\*;%CLASSPATH%

:: JAVA_OPTS
set JAVA_OPTS=-server -Xmx192m -XX:+UseParallelGC -XX:CompressedClassSpaceSize=128m
set JAVA_OPTS=%JAVA_OPTS% -XX:+HeapDumpOnOutOfMemoryError "-XX:HeapDumpPath=%JHFS_HOME%\logs\jhfs.hprof"
set JAVA_OPTS=%JAVA_OPTS% -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=128M "-Xloggc:%JHFS_HOME%\logs\jhfs.gc.log" -XX:+PrintTenuringDistribution -XX:+PrintGCDetails
