#!/bin/bash

APP_NAME=jhfs

systemctl daemon-reload

if [ "$1" = "1" ]; then
    if [ ! -d /var/log/jhfs ]; then
        mkdir /var/log/jhfs
    fi
    chown $APP_NAME:$APP_NAME /var/log/jhfs

    systemctl enable $APP_NAME && systemctl start $APP_NAME && echo "Service $APP_NAME has started"
fi
