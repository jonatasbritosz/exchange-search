# Build
FROM adoptopenjdk/openjdk11-openj9:alpine-slim as build

WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar

# App
FROM adoptopenjdk/openjdk11-openj9:alpine-jre

COPY --from=build /app/build/libs/*.jar /app/

ENTRYPOINT echo "JAVA_OPTS=$JAVA_OPTS" && java $JAVA_OPTS \
    -Djava.security.egd=file:/dev/./urandom \
    -jar /app/*.jar