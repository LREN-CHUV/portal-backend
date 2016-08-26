# Pull base image
FROM maven:3.3.9-jdk-8

MAINTAINER mirco.nasuti@chuv.ch

# Create a user with id 1000, with some luck it should match your user on the host machine.
RUN adduser --quiet --uid 1000 build
USER build

COPY ./docker/builder/build-in-docker.sh /

# Volume
VOLUME /build
# Define working directory
WORKDIR /build

ENTRYPOINT ["/build-in-docker.sh"]
