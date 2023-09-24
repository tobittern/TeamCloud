FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER euler

RUN mkdir -p /euler/platform
RUN mkdir -p /euler/platform/logs
RUN mkdir -p /euler/platform/temp

WORKDIR /euler/platform

EXPOSE 9201

ADD ./target/euler-platform.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
