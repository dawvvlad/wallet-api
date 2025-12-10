FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src src

RUN mvn clean package -DskipTests \
    -Dmaven.test.skip=true \
    -Dmaven.compile.fork=true

FROM build AS layers

WORKDIR /workspace
RUN mkdir -p target/extracted && \
    java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

FROM eclipse-temurin:17-jre-jammy

RUN apt-get update && \
    apt-get install -y curl jq && \
    rm -rf /var/lib/apt/lists/*

RUN groupadd -r appgroup && useradd -r -g appgroup -s /bin/false appuser

WORKDIR /app

COPY --from=layers --chown=appuser:appgroup /workspace/target/extracted/dependencies/ ./
COPY --from=layers --chown=appuser:appgroup /workspace/target/extracted/spring-boot-loader/ ./
COPY --from=layers --chown=appuser:appgroup /workspace/target/extracted/snapshot-dependencies/ ./
COPY --from=layers --chown=appuser:appgroup /workspace/target/extracted/application/ ./

COPY --chown=appuser:appgroup src/main/resources/ ./config/

USER appuser

ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xms512m -Xmx1024m \
               -XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+HeapDumpOnOutOfMemoryError \
               -Djava.security.egd=file:/dev/./urandom \
               -Dfile.encoding=UTF-8"

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher"]