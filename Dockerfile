FROM openjdk:21-jdk-slim-buster

ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational"
ENV NAME="stock-0.0.1.jar"

WORKDIR /app
COPY target/$NAME /app/app.jar
EXPOSE $SERVER_PORT
ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}","$JAVA_OPTS","-jar","/app/app.jar"]