FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER euler

RUN mkdir -p /euler/gateway
RUN mkdir -p /euler/gateway/logs
RUN mkdir -p /euler/gateway/temp

WORKDIR /euler/gateway

EXPOSE 8080

ADD ./target/euler-gateway.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
