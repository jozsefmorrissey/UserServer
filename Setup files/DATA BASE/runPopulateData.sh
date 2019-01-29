#!/bin/bash

# If you want to use my bash password script. It can be found here @
# https://github.com/jozsefmorrissey/BashScripts/blob/master/confidentalInfo.sh
cat ./Oracle/*.sql > ./OracleDBSimpleSetup.sql
cat ./OracleDBSimpleSetup.sql ./Oracle/PopulateData/*.sql > OracleDBPopulateData.sql

user=$(sudo confidentalInfo.sh value UserSrvc dbUser)
password=$(sudo confidentalInfo.sh value UserSrvc dbPass)
sysPassword=$(sudo confidentalInfo.sh value UserSrvc systemDbPass)
echo exit | sqlplus system/$sysPassword@localhost:1521/xe @./OracleDBPopulateData.sql $password $user
