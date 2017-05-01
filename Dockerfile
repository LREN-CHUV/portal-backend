FROM openjdk:8u121-jre-alpine
MAINTAINER Mirco Nasuti <mirco.nasuti@chuv.ch>

ARG BUILD_DATE
ARG VCS_REF
ARG VERSION

ENV DOCKERIZE_VERSION=v0.4.0

RUN apk add --update ca-certificates wget \
    && update-ca-certificates \
    && wget -O /tmp/dockerize.tar.gz https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-${DOCKERIZE_VERSION}.tar.gz \
    && tar -C /usr/local/bin -xzvf /tmp/dockerize.tar.gz \
    && rm -rf /var/cache/apk/* /tmp/*

COPY docker/runner/config/application.tmpl /config/application.tmpl
COPY docker/runner/README.md docker/runner/run.sh /

ENV PORTAL_BACKEND_VERSION=2.1.0

COPY target/portal-backend-$PORTAL_BACKEND_VERSION.jar backend.jar

EXPOSE 8080

ENTRYPOINT ["/run.sh"]

LABEL org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="hbpmip/portal-backend" \
      org.label-schema.description="Java backend for the MIP portal" \
      org.label-schema.url="https://mip.humanbrainproject.eu" \
      org.label-schema.vcs-type="git" \
      org.label-schema.vcs-ref=$VCS_REF \
      org.label-schema.vcs-url="https://github.com/LREN-CHUV/portal-backend" \
      org.label-schema.version="$VERSION" \
      org.label-schema.vendor="LREN CHUV" \
      org.label-schema.license="AGPLv3" \
      org.label-schema.docker.dockerfile="Dockerfile" \
      org.label-schema.memory-hint="2048" \
      org.label-schema.schema-version="1.0"
