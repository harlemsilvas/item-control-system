# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom files
COPY pom.xml .
COPY modules/core/pom.xml modules/core/
COPY modules/api/pom.xml modules/api/
COPY modules/worker/pom.xml modules/worker/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY modules/core/src modules/core/src
COPY modules/api/src modules/api/src
COPY modules/worker/src modules/worker/src

# Build application
RUN mvn clean package -DskipTests -pl modules/api -am

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy jar from build stage
COPY --from=build /app/modules/api/target/item-control-api-*.jar app.jar

# Render exposes PORT env var (default 10000)
ENV PORT=10000
EXPOSE ${PORT}

# Health check - usa PORT dinâmica
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT}/actuator/health || exit 1

# Run application - usa PORT do Render
ENTRYPOINT ["sh", "-c", "java \
  -Djava.security.egd=file:/dev/./urandom \
  -Dserver.port=${PORT} \
  -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} \
  -jar app.jar"]
