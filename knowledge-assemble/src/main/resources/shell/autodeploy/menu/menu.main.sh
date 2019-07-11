#!/bin/bash

echo '系统自动部署脚本'
echo '1、安装JAVA环境'
echo '2、安装NGINX环境'
echo '3、安装REDIS环境'
echo '4、安装TOMCAT'
read -p '请选择:' chose

case $chose in 
'1')
echo 'chose 1'
	sh $(pwd)/menu/menu.java.sh
;;

'2')
echo 'chose 2'
;;

'3')
echo 'chose 3'
;;

'4')
	sh $(pwd)/menu/menu.tomcat.sh
;;

esac

