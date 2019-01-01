#!/bin/bash

# If you want to use my bash password script. It can be found here @
# https://github.com/jozsefmorrissey/BashScripts/blob/master/confidentalInfo.sh

sudo confidentalInfo.sh replace dbinfo UserSrvc ./OracleDBPopulateData.sql

password=$(confidentalInfo.sh value dbinfo system)
echo exit | sqlplus system/$password @./OracleDBPopulateData.sql

sudo confidentalInfo.sh remove dbinfo UserSrvc ./OracleDBPopulateData.sql
