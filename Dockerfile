FROM maven:3.5-jdk-8-alpine AS builder
ADD pom.xml ./pom.xml
ADD src ./src
RUN mvn package -DskipTests

FROM openjdk:8-jre-alpine
COPY --from=builder /target/helloworld-*.jar /helloworld.jar
VOLUME /tmp
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/helloworld.jar"]
