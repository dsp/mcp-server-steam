# Build stage
FROM alpine:latest AS builder

# Install devenv
RUN apk add --no-cache openjdk21 maven

# Copy project files
WORKDIR /build
COPY pom.xml .
COPY src/ src/

RUN ls -alh
# Install dependencies and build
RUN mvn package
RUN ls -alh target

# Runtime stage
FROM alpine:latest AS mcp-server-steam

RUN apk add --no-cache openjdk21-jre
WORKDIR /app
RUN ls -alh
COPY --from=builder /build/target/mcp-steam-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
