#!/bin/bash

# If you want to use my bash password script. It can be found here @
# https://github.com/jozsefmorrissey/BashScripts/blob/master/confidentalInfo.sh

# sudo confidentalInfo.sh replace dbinfo UserSrvc ./OracleDBSimpleSetup.sql

# TODO: sqlplus will not let me switch users. Fix issue so that script will run

password=$(sudo confidentalInfo.sh value UserSrvc dbpass)
# password=$(docker exec confidentialInfo bash -c "./confidentalInfo.sh value UserSrvc dbpass")
echo exit | sqlplus system/42CkyzXzGu3tjTe8@localhost:1521/xe @./OracleDBSimpleSetup.sql $password

# sudo confidentalInfo.sh remove dbinfo UserSrvc ./OracleDBSimpleSetup.sql
