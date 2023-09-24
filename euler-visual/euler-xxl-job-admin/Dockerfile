FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER euler

RUN mkdir -p /euler/xxl-job-admin
RUN mkdir -p /euler/xxl-job-admin/logs

WORKDIR /euler/xxl-job-admin

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 9900

ADD ./target/euler-xxl-job-admin.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
