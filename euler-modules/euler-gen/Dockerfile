FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER euler

RUN mkdir -p /euler/gen
RUN mkdir -p /euler/gen/logs

WORKDIR /euler/gen

EXPOSE 9202

ADD ./target/euler-gen.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
