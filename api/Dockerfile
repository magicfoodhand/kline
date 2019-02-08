# Thanks to http://whitfin.io/speeding-up-maven-docker-builds/
FROM maven:3.6.0-jdk-11 as maven

# copy the project files
COPY ./pom.xml ./pom.xml

# build all dependencies
RUN mvn dependency:go-offline -B

# copy your other files
COPY ./src ./src

# build for release
RUN mvn package

# our final base image
FROM openjdk:11.0.2-jre-stretch

# set deployment directory
WORKDIR /kline-api

# copy over the built artifact from the maven image
COPY --from=maven target/kline-api-*jar-with-dependencies.jar ./kline-api.jar

# set the startup command to run your binary
CMD ["java", "-jar", "kline-api.jar"]