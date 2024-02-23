FROM openjdk:17-oracle

# Copying the JAR file into the container
COPY target/country-service.jar country-service.jar

# Exposing port 8080
EXPOSE 8080

# Defining the entry point to run your application
ENTRYPOINT ["java", "-jar", "country-service.jar"]
