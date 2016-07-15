FROM java:openjdk-8u92-jdk-alpine

VOLUME /config

COPY ./target/backend-services-DOCKER_BUILD.jar backend.jar
CMD ["java", "-jar", "backend.jar"]
