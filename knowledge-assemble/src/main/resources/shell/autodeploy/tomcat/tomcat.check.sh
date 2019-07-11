#!/bin/base

COMMON_ECHO=$(pwd)/common/common.echo.sh

checkTomcatRuning() {
	TOMCAT_PID=$(ps -ef|grep tomcat|grep -w '/usr/local/tomcat-ksbd-uat'|grep -v 'grep'|awk '{print $2}')
	if [ $TOMCAT_PID ]; then
		sh $COMMON_ECHO echoGreen "tomcat service is running!"
		TOMCAT_SERVICE_CODE=$(curl -s -o /root/tomcat.log -m 10 --connect-timeout 10 http://127.0.0.1:8080/ -w %{http_code})
		if [ $TOMCAT_SERVICE_CODE -eq 200 ]; then 
			sh $COMMON_ECHO echoGreen "tomcat service is available!"
		else
			sh $COMMON_ECHO echoGreen "tomcat service is unavailable!"
		fi
		return ${TOMCAT_SERVICE_CODE}
	else
		sh $COMMON_ECHO echoGreen "tomcat service is stop"
		return 0
	fi
	
}

checkTomcatRuning
