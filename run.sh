#!/bin/bash
java -jar ./target/server-0.0.1-SNAPSHOT.jar

cd ./universal-user-client && mvn install &
cd ./server && mvn spring-boot:run
