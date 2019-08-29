#!/bin/bash

source ../../BashScripts/commandParser.sh
source ../../BashScripts/properties.sh

user=${flags[user]}
password=${flags[password]}
unset flags[password]
unset flags[user]

propFile=../../server/src/main/resources/application.properties
host=$(getValue host $propFile)
database=$(getValue database $propFile)
rootPassword=$(pst value system mysql)

if [ -z $user ]
then
  testPropFile=../../server/src/main/resources/application-test.properties
  password=$(getValue spring.datasource.password $testPropFile)
  user=$(getValue spring.datasource.username $testPropFile)
fi

echo -e "user $user\npassword $password"
./run.sh -type mySql -host $host -database $database -user $user \
          -password $password -rootPassword $rootPassword  $(boolStr) $(flagStr)
