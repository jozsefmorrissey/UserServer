From sath89/oracle-12c

ADD ./fixEntry.sh /
ADD . /Oracle/
WORKDIR /Oracle/

RUN   echo hello world && \
  apt-get update && \
  apt-get -y install libaio1 unzip && \
  apt-get clean && \
  rm -rf /tmp/* /var/lib/apt/lists/* /var/tmp/*
