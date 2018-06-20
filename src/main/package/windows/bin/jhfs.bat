@echo off

call setenv.bat

pushd .

cd "%JHFS_HOME%"
java %JAVA_OPTS% com.github.ladutsko.jhfs.Application

popd
