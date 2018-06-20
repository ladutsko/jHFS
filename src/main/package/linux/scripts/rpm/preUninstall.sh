#!/bin/bash

APP_NAME=jhfs

systemctl stop $APP_NAME && echo "Service $APP_NAME is stopped"

if [ "$1" = "0" ]; then
    systemctl disable $APP_NAME
fi
