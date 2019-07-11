#!/bin/base

TOMCAT_INSTALL_DIR=/usr/local
SOFTWARE_DIR=$(pwd)/software
COMMON_ECHO=$(pwd)/common/common.echo.sh
TOMCAT_DOWN_URL='http://mirrors.ocf.berkeley.edu/apache/tomcat/tomcat-7/v7.0.94/bin/apache-tomcat-7.0.94.tar.gz'

if [ ! -d $SOFTWARE_DIR ]; then
	mkdir -r $SOFTWARE_DIR
fi

if [ ! -f $SOFTWARE_DIR/apache-tomcat-7.0.94.tar.gz ]; then
	sh $COMMON_ECHO echoRed "JAVA>>安装JAVA环境>>下载jdk1.7的安装文件"
	wget -P $SOFTWARE_DIR $TOMCAT_DOWN_URL
	sh $COMMON_ECHO echoRed "JAVA>>安装JAVA环境>>文件下载成功，开始解压并配置环境变量"
else
	sh $COMMON_ECHO echoRed "JAVA>>安装JAVA环境>>已存在安装文件，跳过下载，开始解压并配置环境变量"
fi

tar zxvf $SOFTWARE_DIR/apache-tomcat-7.0.94.tar.gz -C $SOFTWARE_DIR
mv $SOFTWARE_DIR/apache-tomcat-7.0.94 $SOFTWARE_DIR/tomcat-ksbd-uat
mv $SOFTWARE_DIR/tomcat-ksbd-uat/ $TOMCAT_INSTALL_DIR

