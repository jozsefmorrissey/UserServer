#!/bin/bash

# If you want to use my bash password script. It can be found here @
# https://github.com/jozsefmorrissey/BashScripts/blob/master/confidentalInfo.sh
source ../../BashScripts/commandParser.sh

cat ./Oracle/*.sql > ./sql/OracleDBSimpleSetup.sql
cat ./sql/OracleDBSimpleSetup.sql ./Oracle/PopulateData/*.sql > ./sql/OracleDBPopulateData.sql

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
dbUrl=$(sudo confidentalInfo.sh value UserSrvc DB_URL)
echo $password : $user : $dbUrl : $sysPassword
if [ "${booleans[simple]}" != "true" ]
then
  echo exit | sqlplus system/$sysPassword@$dbUrl @./sql/OracleDBPopulateData.sql $password $user
else
  echo exit | sqlplus system/$sysPassword@$dbUrl @./sql/OracleDBSimpleSetup.sql $password $user
fi
