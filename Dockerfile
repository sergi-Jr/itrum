FROM gradle:8-jdk21 AS builder

WORKDIR /app
COPY app/ .

RUN gradle clean build --no-daemon

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN apt-get update && apt-get install -y curl

HEALTHCHECK --interval=7s --timeout=5s --retries=10 CMD curl -k http://localhost:8800/actuator/health | grep -q 'UP' || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
