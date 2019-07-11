#!/bin/bash

COMMON_ECHO=$(pwd)/common/common.echo.sh

echo '1、安装TOMCAT'
echo '2、返回上一级菜单'
read -p '请选择:' chose

case $chose in 
'1')
	sh $COMMON_ECHO echoRed "----------------------检查当前JAVA环境--------------------"
	sh $(pwd)/tomcat/tomcat.install.sh
	sh $(pwd)/menu/menu.tomcat.sh
;;

'2')
	sh $(pwd)/menu/menu.main.sh
;;

esac

