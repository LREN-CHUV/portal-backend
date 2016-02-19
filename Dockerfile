# This Dockerfile encapsulate the MIP portal backend application for development purposes.
# We use it to bootstrap the generation of the Hibernate mapping file and the application jar.

FROM maven:3-jdk-8

COPY ./src/docker/init/build-in-docker.sh /build-in-docker.sh

CMD mkdir /opt/portal/

VOLUME /opt/portal/

WORKDIR /opt/portal/

CMD ["/build-in-docker.sh"]
