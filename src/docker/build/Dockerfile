FROM maven:3-jdk-8

MAINTAINER mirco.nasuti@chuv.ch

COPY ./build-in-docker.sh /build-in-docker.sh
COPY ./certs/hbps1.crt /hbps1.crt

VOLUME /opt/portal/
WORKDIR /opt/portal/

CMD ["bash", "/build-in-docker.sh"]
