# This Dockerfile encapsulate the MIP portal  backend application for development purposes.

FROM java:8-jre

RUN apt-get update && apt-get install -y wget
RUN wget https://github.com/jwilder/dockerize/releases/download/v0.2.0/dockerize-linux-amd64-v0.2.0.tar.gz
RUN tar -C /usr/local/bin -xzvf dockerize-linux-amd64-v0.2.0.tar.gz

RUN mkdir -p /opt/portal/ \
    && echo -n "#!/bin/sh\n/usr/bin/java -DconfigFile=/opt/portal/config/portal.properties -jar /opt/portal/lib/mip.jar" > /opt/portal/mip.sh \
    && chmod +x /opt/portal/mip.sh
EXPOSE 8080

VOLUME /opt/portal/config/
VOLUME /opt/portal/lib/

CMD ["/usr/local/bin/dockerize", "-wait", "tcp://portaldb:5432", "/opt/portal/mip.sh"]
