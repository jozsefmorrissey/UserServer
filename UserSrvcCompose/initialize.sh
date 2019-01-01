#!/bin/bash

source ../bashLib/properties.sh
ENV_FILE=./.env

DB_TAG=$(getValue DB_TAG $ENV_FILE)
DB_HOST_LOC=$(getValue DB_HOST_LOC $ENV_FILE)
DB_CONT_LOC=$(getValue DB_CONT_LOC $ENV_FILE)
DB_PORT=$(getValue DB_PORT $ENV_FILE)
DB_ALT_PORT=$(getValue DB_ALT_PORT $ENV_FILE)

initializeDB() {
  initName=initialize-db
  sudo docker run --name $initName -p $DB_ALT_PORT:8080 -p $DB_PORT:1521 -v $DB_HOST_LOC:$DB_CONT_LOC -d $DB_TAG
  counter=1
  sleep 5
  while [ $counter -le 10 ]
  do
    percent=$(sudo docker logs $initName | grep -oP "[0-9]{1,3}% complete.*" | sed -E "s/([0-9]{1,3}).*/\1/" | tr "\r\n" " " | sed -E "s/.* ([0-9].*?) /\1/")
    if [ "$percent" ];
    then
      let "time=102-$percent"
    else
      time=2
    fi
    sleep $time
    serverRunning=$(sudo docker logs $initName | grep "Database ready to use. Enjoy")
  	if [ "$serverRunning" ]; then
      echo server running
      counter=11;
  	fi
  done
  sudo docker rm -f $initName
}

echo \"$DB_CONT_LOC\"
initializeDB
