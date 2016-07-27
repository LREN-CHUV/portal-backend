FROM java:openjdk-8u92-jdk-alpine

MAINTAINER mirco.nasuti@chuv.ch

COPY ./target/portal-backend-DOCKER_BUILD.jar backend.jar

RUN apk add --update ca-certificates wget &&  rm -rf /var/cache/apk/* /tmp/* && update-ca-certificates
RUN wget https://github.com/jwilder/dockerize/releases/download/v0.2.0/dockerize-linux-amd64-v0.2.0.tar.gz
RUN tar -C /usr/local/bin -xvzf dockerize-linux-amd64-v0.2.0.tar.gz

CMD mkdir /config
ADD ./config/application.tmpl /config/application.tmpl

CMD dockerize -template /config/application.tmpl:/config/application.yml java -jar backend.jar
