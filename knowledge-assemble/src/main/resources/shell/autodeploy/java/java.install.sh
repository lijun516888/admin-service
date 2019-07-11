#!/bin/base

JDK_INSTALL_DIR="/opt/jdk1.7"
SOFTWARE_DIR=$(pwd)/software
COMMON_ECHO=$(pwd)/common/common.echo.sh
JDK_DOWNLOAD_URL=https://download.oracle.com/otn/java/jdk/7u80-b15/jdk-7u80-linux-x64.tar.gz?AuthParam=1562831581_d47212410006bbe9e9c2abc9b249c560

if [ ! -d $SOFTWARE_DIR ]; then
	mkdir -r $SOFTWARE_DIR
fi

if [ ! -f $SOFTWARE_DIR/jdk-7u80-linux-x64.tar.gz ]; then
	sh $COMMON_ECHO echoRed "JAVA>>安装JAVA环境>>下载jdk1.7的安装文件"
	wget -P $SOFTWARE_DIR $JDK_DOWNLOAD_URL
	sh $COMMON_ECHO echoRed "JAVA>>安装JAVA环境>>文件下载成功，开始解压并配置环境变量"
else
	sh $COMMON_ECHO echoRed "JAVA>>安装JAVA环境>>已存在安装文件，跳过下载，开始解压并配置环境变量"
fi

if [ ! -d $JDK_INSTALL_DIR ]; then
	mkdir -p $JDK_INSTALL_DIR
fi

tar zxvf $SOFTWARE_DIR/jdk-7u80-linux-x64.tar.gz -C $SOFTWARE_DIR
mv $SOFTWARE_DIR/jdk1.7.0_80/* $JDK_INSTALL_DIR

echo 'export JAVA_HOME=/opt/jdk1.7' >> /etc/profile
echo 'export PATH=$PATH:$JAVA_HOME/bin' >> /etc/profile
source /etc/profile

java -version

