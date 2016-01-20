FROM java:8-jre

COPY target/mip.jar /opt/mip/mip.jar

EXPOSE 8080

VOLUME /opt/portal/config/

CMD ["/usr/bin/java", "-DconfigFile=/opt/portal/config/portal.properties", -jar", "/opt/mip/mip.jar"]
