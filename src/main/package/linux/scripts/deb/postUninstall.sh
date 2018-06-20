#!/bin/bash

APP_NAME=jhfs

if [ "$1" = "purge" ]; then
    getent group $APP_NAME && gpasswd -M "" $APP_NAME
    getent passwd $APP_NAME && userdel $APP_NAME && echo "User $APP_NAME is deleted"
    getent group $APP_NAME && groupdel $APP_NAME && echo "Group $APP_NAME is deleted"
fi

exit 0
