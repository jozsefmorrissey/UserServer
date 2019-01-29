From maven

ADD . /UserSrvc/
ADD ./.m2-supplement/ /root/.m2/
WORKDIR /UserSrvc/
RUN mvn package
FROM openjdk:7
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac Main.java
CMD ["java", "Main"]
