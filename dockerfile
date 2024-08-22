FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/finalProject_synrgy-0.0.1-SNAPSHOT.jar /app/synrgy-final-project.jar

EXPOSE 8080

USER 0

RUN mkdir /data

ENTRYPOINT ["java", "-jar", "/app/synrgy-final-project.jar"]