FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER euler

RUN mkdir -p /euler/monitor
RUN mkdir -p /euler/monitor/logs

WORKDIR /euler/monitor

EXPOSE 9100

ADD ./target/euler-monitor.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
