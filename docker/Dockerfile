FROM openjdk:11-jre-slim

FROM maven:3.6.3-jdk-11 AS MAVEN_BUILD

RUN apt-get update && apt-get install python2.7
COPY setup.sh /root/greenpole/setup.sh
RUN chmod +x /root/greenpole/setup.sh
RUN /root/greenpole/setup.sh

COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package -U -Dmaven.test.skip=true
RUN ls /build/target
RUN cp /build/target/transaction-component-0.0.1-RELEASE.jar /opt/greenpole


WORKDIR /

COPY install.sh /root/greenpole/install.sh
RUN chmod +x /root/greenpole/install.sh
CMD  /root/greenpole/install.sh