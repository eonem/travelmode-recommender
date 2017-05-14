FROM java:8-jre
MAINTAINER Evren Onem <evren.onem@gmail.com>

COPY ./target/travel-mode-recommender-config-server-0.0.1-SNAPSHOT.jar /app/config-server.jar

CMD ["java", "-Xmx256m", "-jar", "/app/config-server.jar"]

EXPOSE 8888