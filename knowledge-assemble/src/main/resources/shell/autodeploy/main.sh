#!/bin/bash

echoRed() {
	echo $'\e[0;31m'$1$'\e[0m';
}
echoGeen() {
	$'\e[0;31m'$1$'\e[0m';	
}
echoYellow() {
	$'\e[0;31m'$1$'\e[0m';	
}

#打印主菜单
sh ./menu/menu.main.sh

