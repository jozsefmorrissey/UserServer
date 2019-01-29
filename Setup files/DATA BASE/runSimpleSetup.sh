#!/bin/bash

# If you want to use my bash password script. It can be found here @
# https://github.com/jozsefmorrissey/BashScripts/blob/master/confidentalInfo.sh

# TODO: sqlplus will not let me switch users. Fix issue so that script will run
cat ./Oracle/*.sql > ./OracleDBSimpleSetup.sql

user=$(sudo confidentalInfo.sh value UserSrvc dbUser)
password=$(sudo confidentalInfo.sh value UserSrvc dbPass)
sysPassword=$(sudo confidentalInfo.sh value UserSrvc systemDbPass)
echo exit | sqlplus system/$sysPassword@localhost:1521/xe @./OracleDBSimpleSetup.sql $password $user
