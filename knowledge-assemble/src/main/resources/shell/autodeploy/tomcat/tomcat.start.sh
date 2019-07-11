#!/bin/base

COMMON_ECHO=$(pwd)/common/common.echo.sh

sh $(pwd)/tomcat/tomcat.check.sh

echo $TOMCAT_PID
echo $TOMCAT_SERVICE_CODE
echo $?
if [ $? -eq 200 ]; then
	echo 'if'		
else
	echo 'else'
fi
