FROM frolvlad/alpine-oraclejdk8:slim

COPY ./target/backend-services-DOCKER_BUILD.jar backend.jar
CMD ["java", "-jar", "backend.jar"]
