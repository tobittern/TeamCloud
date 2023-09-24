FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER euler

RUN mkdir -p /euler/system
RUN mkdir -p /euler/system/logs
RUN mkdir -p /euler/system/temp

WORKDIR /euler/system

EXPOSE 9201

ADD ./target/euler-system.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
