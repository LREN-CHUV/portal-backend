FROM java:openjdk-8u92-jdk-alpine

MAINTAINER mirco.nasuti@chuv.ch

RUN apk add --update ca-certificates wget &&  rm -rf /var/cache/apk/* /tmp/* && update-ca-certificates
RUN wget https://github.com/jwilder/dockerize/releases/download/v0.2.0/dockerize-linux-amd64-v0.2.0.tar.gz
RUN tar -C /usr/local/bin -xvzf dockerize-linux-amd64-v0.2.0.tar.gz

COPY ./docker/runner/config/application.tmpl /config/application.tmpl
COPY ./docker/runner/README.md /
COPY ./target/portal-backend-DOCKER_BUILD.jar backend.jar

EXPOSE 8080

CMD ["dockerize", "-template", "/config/application.tmpl:/config/application.yml", "java", "-jar", "backend.jar"]
