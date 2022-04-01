FROM openjdk:17-jdk-slim-buster
ENV TZ="Europe/Istanbul"
RUN date
COPY ./target/carpark-0.0.1-SNAPSHOT.jar carpark-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/carpark-0.0.1-SNAPSHOT.jar"]