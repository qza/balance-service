FROM java:8-jre
COPY app.yml /opt/dwizbalanceservice/
COPY target/balance-service-app-1.0-SNAPSHOT.jar /opt/dwizbalanceservice/
EXPOSE 8080
WORKDIR /opt/dwizbalanceservice
CMD ["java", "-jar", "-Done-jar.silent=true", "balance-service-app-1.0-SNAPSHOT.jar", "server", "app.yml"]