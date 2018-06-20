#!/bin/bash

APP_NAME=jhfs

if [ "$1" = "install" ]; then
    getent group $APP_NAME || groupadd -r $APP_NAME
    getent passwd $APP_NAME || useradd -r -g $APP_NAME -s /bin/nologin $APP_NAME 

    if [ ! -d /var/jhfs/welcome ]; then
        mkdir -p /var/jhfs/welcome
    fi
    chown -R $APP_NAME:$APP_NAME /var/jhfs
fi
