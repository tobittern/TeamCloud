FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER euler

RUN mkdir -p /euler/sentinel-dashboard
RUN mkdir -p /euler/sentinel-dashboard/logs

WORKDIR /euler/sentinel-dashboard

EXPOSE 8718

ADD ./target/euler-sentinel-dashboard.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
