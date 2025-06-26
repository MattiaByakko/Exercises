FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/pcshop-api.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
