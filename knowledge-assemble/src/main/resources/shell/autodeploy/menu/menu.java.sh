#!/bin/bash

COMMON_ECHO=$(pwd)/common/common.echo.sh

echo 'JAVA环境安装菜单'
echo '1、检查当前JAVA环境'
echo '2、安装JAVA环境'
echo '3、返回上一级菜单'
read -p '请选择:' chose

case $chose in 
'1')
	sh $COMMON_ECHO echoRed "----------------------检查当前JAVA环境--------------------"
	sh $(pwd)/java/java.exists.sh
	sh $(pwd)/menu/menu.java.sh
;;

'2')
	sh $COMMON_ECHO echoRed "----------------------安装JAVA环境--------------------"
	sh $(pwd)/java/java.install.sh
	sh $(pwd)/menu/menu.java.sh
;;

'3')
	sh $(pwd)/menu/menu.main.sh
;;

esac

