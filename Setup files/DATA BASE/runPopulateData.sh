#!/bin/bash

# If you want to use my bash password script. It can be found here @
# https://github.com/jozsefmorrissey/BashScripts/blob/master/confidentalInfo.sh
cat ./Oracle/*.sql > ./OracleDBSimpleSetup.sql
cat ./OracleDBSimpleSetup.sql ./Oracle/PopulateData/*.sql > OracleDBPopulateData.sql

user=$1
password=$2
if [ -z $1 ]
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
sysPassword=$(sudo confidentalInfo.sh value UserSrvc systemDbPass)
dbUrl=$(sudo confidentalInfo.sh value UserSrvc dbUrl)
echo exit | sqlplus system/$sysPassword@$dbUrl @./OracleDBPopulateData.sql $password $user
