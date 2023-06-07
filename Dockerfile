FROM eclipse-temurin:17-jre
COPY build/libs/vaadin-embedded-jetty-gradle-v23-all.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "vaadin-embedded-jetty-gradle-v23-all.jar"]