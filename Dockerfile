FROM openjdk:17
ADD /target/Link-shortening-service.jar Link-shortening-service.jar

ENTRYPOINT ["java", "-jar", "Link-shortening-service.jar"]