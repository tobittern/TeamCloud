FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER euler

RUN mkdir -p /euler/auth
RUN mkdir -p /euler/auth/logs
RUN mkdir -p /euler/auth/temp

WORKDIR /euler/auth

EXPOSE 9200

ADD ./target/euler-auth.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
