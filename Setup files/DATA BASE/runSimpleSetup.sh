#!/bin/bash

# If you want to use my bash password script. It can be found here @
# https://github.com/jozsefmorrissey/BashScripts/blob/master/confidentalInfo.sh

source ../../BashScripts/commandParser.sh

# TODO: sqlplus will not let me switch users. Fix issue so that script will run
cat ./Oracle/*.sql > ./OracleDBSimpleSetup.sql

sysPassword=$(confidentalInfo.sh value UserSrvc systemDbPass)
echo sys: $sysPassword

if [ "${booleans[test]}" == "true" ]
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
  dbUrl=$(confidentalInfo.sh value UserSrvc DB_URL)

  echo sqlplus system/$sysPassword@$dbUrl @./OracleDBSimpleSetup.sql $password $user
  echo exit | sqlplus system/$sysPassword@$dbUrl @./OracleDBSimpleSetup.sql $password $user
fi

if [ "${booleans[primary]}" == "true" ]
then
  user=$(confidentalInfo.sh value UserSrvc dbUser)
  password=$(confidentalInfo.sh value UserSrvc dbPass)
  dbUrl=$(confidentalInfo.sh value UserSrvc dbUrl)

  echo sqlplus system/$sysPassword@$dbUrl @./OracleDBSimpleSetup.sql $password $user
  echo exit | sqlplus system/$sysPassword@$dbUrl @./OracleDBSimpleSetup.sql $password $user
fi
