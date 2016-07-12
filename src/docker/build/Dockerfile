FROM maven:3-jdk-8

COPY ./build-in-docker.sh /build-in-docker.sh

VOLUME /opt/portal/
WORKDIR /opt/portal/

RUN apt-get update && apt-get install -y postgresql

CMD ["bash", "/build-in-docker.sh"]
