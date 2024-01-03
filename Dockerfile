FROM eclipse-temurin:17.0.4_8-jre-alpine
COPY /build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]