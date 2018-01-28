# Build stage for Java classes
FROM hbpmip/java-base-build:3.5.0-jdk-8-9 as java-build-env

COPY pom.xml /project/

RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn clean package

COPY src/ /project/src/

# Repeating the file copy works better. I dunno why.
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn clean package

FROM hbpmip/java-base:8u151-0
MAINTAINER Mirco Nasuti <mirco.nasuti@chuv.ch>

ARG BUILD_DATE
ARG VCS_REF
ARG VERSION

COPY docker/config/application.tmpl /config/application.tmpl
COPY docker/README.md docker/run.sh /

COPY --from=java-build-env /project/target/portal-backend.jar /usr/share/jars/

EXPOSE 8080

ENTRYPOINT ["/run.sh"]

LABEL org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="hbpmip/portal-backend" \
      org.label-schema.description="Java backend for the MIP portal" \
      org.label-schema.url="https://mip.humanbrainproject.eu" \
      org.label-schema.vcs-type="git" \
      org.label-schema.vcs-url="https://github.com/LREN-CHUV/portal-backend" \
      org.label-schema.vcs-ref=$VCS_REF \
      org.label-schema.version="$VERSION" \
      org.label-schema.vendor="LREN CHUV" \
      org.label-schema.license="AGPLv3" \
      org.label-schema.docker.dockerfile="Dockerfile" \
      org.label-schema.memory-hint="2048" \
      org.label-schema.schema-version="1.0"
