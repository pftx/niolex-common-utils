#!/bin/sh
#remove old records from database
idate=`date -d -2day +"%Y-%m-%d %H:%M:%S"`
/usr/bin/mysql -u root -pMooseFS -D test -e "delete from abc where last_update_time < '$idate'"
echo "delete old database records status: $?."
