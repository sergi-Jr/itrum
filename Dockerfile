FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/itrum-0.0.1.jar app.jar

RUN apt-get update && apt-get install -y curl
HEALTHCHECK --interval=7s --timeout=5s --retries=10 CMD curl -k http://localhost:8800/actuator/health | grep -q 'UP' || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]