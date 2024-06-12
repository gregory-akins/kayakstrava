## Use AWS AL2 + Corretto base image
FROM amazoncorretto:17-al2-jdk

RUN ls

USER 1000

HEALTHCHECK --interval=5m --timeout=3s CMD curl -f http://localhost:8080/actuator/health/ || exit 1

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar


## Launch the wait tool and then your application
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "app.jar"]
