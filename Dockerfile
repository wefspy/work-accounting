FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x ./mvnw && \
    ./mvnw dependency:go-offline -ntp

COPY src src
RUN ./mvnw package -DskipTests -Dmaven.test.skip=true -ntp

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl tini && \
    addgroup -S spring && \
    adduser -S spring -G spring && \
    mkdir -p /app/logs && \
    chown -R spring:spring /app

COPY --from=builder /workspace/app/target/*.jar app.jar
RUN chown spring:spring app.jar

USER spring:spring

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+OptimizeStringConcat \
    -XX:+UseStringDeduplication \
    -Djava.security.egd=file:/dev/./urandom"

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=5 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1


ENTRYPOINT ["/sbin/tini", "--"]
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]