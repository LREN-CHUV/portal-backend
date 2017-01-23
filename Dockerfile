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

ENV PORTAL_BACKEND_VERSION=1.1

COPY target/portal-backend-$PORTAL_BACKEND_VERSION.jar backend.jar

# org.label-schema.build-date=$BUILD_DATE
# org.label-schema.vcs-ref=$VCS_REF
LABEL org.label-schema.schema-version="1.0" \
        org.label-schema.license="AGPLv3" \
        org.label-schema.name="portal-backend" \
        org.label-schema.description="Java backend for the MIP portal" \
        org.label-schema.url="https://mip.humanbrainproject.eu" \
        org.label-schema.vcs-type="git" \
        org.label-schema.vcs-url="https://github.com/LREN-CHUV/portal-backend" \
        org.label-schema.vendor="LREN CHUV" \
        org.label-schema.docker.dockerfile="Dockerfile" \
        org.label-schema.memory-hint="2048"

EXPOSE 8080

CMD ["./run.sh"]
