#!/bin/bash


echoRed() { echo $'\e[0;31m'$1$'\e[0m'; }
echoGreen() { echo $'\e[0;31m'$1$'\e[0m'; }
echoYellow() { echo $'\e[0;31m'$1$'\e[0m'; }
$1 "$2"
