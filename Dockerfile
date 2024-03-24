FROM gradle:jdk21-alpine AS build
COPY --chown=gradle:gradle . /src
WORKDIR /src
RUN gradle build --no-daemon

FROM openjdk:21
RUN mkdir /app
COPY --from=build /src/build/libs/trb-core-0.0.1.jar /app/trb-core-0.0.1.jar

ENTRYPOINT ["java", "--enable-preview", "-jar", "/app/trb-core-0.0.1.jar"]