#!/bin/bash

# If you want to use my bash password script. It can be found here @
# https://github.com/jozsefmorrissey/BashScripts/blob/master/confidentalInfo.sh

# TODO: sqlplus will not let me switch users. Fix issue so that script will run
cat ./Oracle/*.sql > ./OracleDBSimpleSetup.sql

user=$(confidentalInfo.sh value UserSrvc dbUser)
password=$(confidentalInfo.sh value UserSrvc dbPass)
if [ -z $password ]
then
  propFile=../../server/src/main/resources/application-test.properties
  password=$(
    grep "spring.datasource.password=.*" $propFile |
    sed "s/.*=\(.*\)/\1/"
  )
  user=$(
    grep "spring.datasource.username=.*" $propFile |
    sed "s/.*=\(.*\)/\1/"
  )
fi
sysPassword=$(confidentalInfo.sh value UserSrvc systemDbPass)
dbUrl=$(confidentalInfo.sh value UserSrvc dbUrl)
echo sqlplus system/$sysPassword@$dbUrl @./OracleDBSimpleSetup.sql $password $user
echo exit | sqlplus system/$sysPassword@$dbUrl @./OracleDBSimpleSetup.sql $password $user
