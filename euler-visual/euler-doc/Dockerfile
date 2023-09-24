FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER euler

RUN mkdir -p /euler/doc
RUN mkdir -p /euler/doc/logs

WORKDIR /euler/doc

EXPOSE 18000

ADD ./target/euler-doc.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
