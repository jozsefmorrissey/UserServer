#!/bin/bash
mvn package
java -jar ./target/server-0.0.1-SNAPSHOT.jar $(sudo confidentalInfo.sh value UserSrvc token)
