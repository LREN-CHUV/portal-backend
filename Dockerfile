FROM java:openjdk-8u92-jdk-alpine

MAINTAINER mirco.nasuti@chuv.ch

ENV DOCKERIZE_VERSION=v0.2.0

RUN apk add --update ca-certificates wget \
    && rm -rf /var/cache/apk/* /tmp/* \
    && update-ca-certificates \
    && wget -O /tmp/dockerize.tar.gz https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-${DOCKERIZE_VERSION}.tar.gz \
    && tar -C /usr/local/bin -xzvf /tmp/dockerize.tar.gz \
    && rm -rf /tmp/dockerize.tar.gz

COPY docker/runner/config/application.tmpl /config/application.tmpl
COPY docker/runner/README.md docker/runner/run.sh /
COPY target/portal-backend-1.0.0-docker-packaging.jar backend.jar

EXPOSE 8080

CMD ["./run.sh"]
