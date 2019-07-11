#!/bin/base

COMMON_ECHO=$(pwd)/common/common.echo.sh

checkExistsJava() {
	java -version
	# $?表示获取上一个命令执行的结果，成功返回0，失败返回其它值
	if [ $? = 0 ]; then
		sh $COMMON_ECHO echoGreen "------------------当前环境变量中已配置JAVA环境！--------------------"
	else
		sh $COMMON_ECHO echoRed "----------------当前未配置JAVA环境变----------------------"
	fi
}

checkExistsJava
