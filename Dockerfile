FROM java:8-jre

COPY target/mip.jar /opt/mip/mip.jar

EXPOSE 8080

CMD ["/usr/bin/java", "-jar", "/opt/mip/mip.jar"]
