#!/bin/bash
token=$(echo "$1" | sudo -S confidentalInfo.sh value UserSrvc token)
port=$(echo "$1" | sudo -S confidentalInfo.sh value UserSrvc CONFIG_PORT)
java -jar ./target/server-0.0.1-SNAPSHOT.jar $token $port
