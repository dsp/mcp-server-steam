# Build stage
FROM alpine:latest AS builder

# Add build arg
ARG VERSION=1.0-SNAPSHOT

# Install devenv
RUN apk add --no-cache openjdk21 maven

# Copy project files
WORKDIR /build
COPY pom.xml .
COPY src/ src/

# Build with version
RUN mvn package -Dversion=$VERSION

# Find the jar with dependencies (most reliable approach)
RUN mv $(find target -name "mcp-steam-*.jar") target/app.jar

# Runtime stage
FROM alpine:latest AS mcp-server-steam

RUN apk add --no-cache openjdk21-jre
WORKDIR /app
COPY --from=builder /build/target/app.jar app.jar

CMD ["java", "-jar", "app.jar"]
