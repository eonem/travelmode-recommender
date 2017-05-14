FROM java:8-jre
MAINTAINER Evren Onem <evren.onem@gmail.com>

COPY ./target/travel-mode-recommender-service-0.0.1-SNAPSHOT.jar /app/tmr-api.jar

CMD ["java", "-Xmx256m", "-jar", "/app/tmr-api.jar"]

EXPOSE 8080