FROM openjdk:8-jdk-alpine

ADD target/*.jar app.jar

ENV JAVA_OPTS=""
ENV SPRING_PROFILE="prod"

ENTRYPOINT exec java $JAVA_OPTS \
 -Dspring.profiles.active=$SPRING_PROFILE \
 -jar app.jar