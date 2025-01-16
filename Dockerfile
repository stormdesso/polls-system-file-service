FROM maven:3.9.4-eclipse-temurin-21 as builder
WORKDIR /app
COPY pom.xml ./
COPY src ./src/

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt
COPY --from=builder /app/target/*.jar polls-system-file-service.jar

EXPOSE 8082

ENTRYPOINT ["java", "-Drun.jvmArguments=", "-Dfile.encoding=UTF-8", "-Xms256m", "-Xmx2048m", "-jar", "/opt/polls-system-file-service.jar"]
