#!/bin/bash

APP_NAME=jhfs

systemctl stop $APP_NAME && echo "Service $APP_NAME is stopped"

if [ "$1" = "remove" ]; then
    systemctl disable $APP_NAME
fi
