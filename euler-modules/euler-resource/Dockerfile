FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER euler

RUN mkdir -p /euler/resource
RUN mkdir -p /euler/resource/logs
RUN mkdir -p /euler/resource/temp

WORKDIR /euler/resource

EXPOSE 9300

ADD ./target/euler-resource.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
