# This Dockerfile encapsulate the MIP portal  backend application for development purposes.

FROM maven:3-jdk-8

RUN apt-get update && apt-get install -y wget
RUN wget https://github.com/jwilder/dockerize/releases/download/v0.2.0/dockerize-linux-amd64-v0.2.0.tar.gz
RUN tar -C /usr/local/bin -xzvf dockerize-linux-amd64-v0.2.0.tar.gz

COPY mip.sh /opt/portal/
RUN chmod +x /opt/portal/mip.sh
EXPOSE 8080

VOLUME /opt/portal/config/
VOLUME /opt/portal/lib/

WORKDIR /opt/portal/

CMD ["/usr/local/bin/dockerize", "-timeout", "240s", "-wait", "tcp://portaldb:5432", "/opt/portal/mip.sh"]
