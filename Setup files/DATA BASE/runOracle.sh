#!/bin/bash

source ../../BashScripts/commandParser.sh
source ../../BashScripts/properties.sh

user=${flags[user]}
password=${flags[password]}

propFile=../../server/src/main/resources/application.properties
host=$(getValue host $propFile)
database=$(getValue database $propFile)
rootPassword=$(sudo confidentalInfo.sh value UserSrvc systemDbPass)
url=$(sudo confidentalInfo.sh value UserSrvc DB_URL)

if [ -z $user ]
then
  testPropFile=../../server/src/main/resources/application-test.properties
  password=$(getValue spring.datasource.password $testPropFile)
  user=$(getValue spring.datasource.username $testPropFile)
fi

./run.sh -type Oracle -user $user -password $password -rootPassword $rootPassword -url $url $(boolStr) $(flagStr)
