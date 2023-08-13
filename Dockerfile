FROM openjdk:17
ADD /target/Link-shortening-service-3.1.1.jar Link-shortening-service.jar

ENTRYPOINT ["java", "-jar", "Link-shortening-service.jar"]