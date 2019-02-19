#!/bin/bash
mvn package
token=$(confidentalInfo.sh value UserSrvc token)
port=$(confidentalInfo.sh value HLWA CONFIG_PORT)


java -jar ./target/server-0.0.1-SNAPSHOT.jar $token $port
