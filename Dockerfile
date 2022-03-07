FROM gradle:7.3.3-jdk11-alpine AS build-image
WORKDIR /home/app
COPY . /home/app
RUN gradle --no-daemon build

# SPRING BOOT
FROM openjdk:11.0.9.1-jre
VOLUME /tmp
EXPOSE 8080
COPY --from=build-image /home/app/build/libs/PeregrineApp-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
