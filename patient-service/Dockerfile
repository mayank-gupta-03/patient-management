FROM maven:3.9.14-eclipse-temurin-25 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package

FROM eclipse-temurin:latest AS runner

WORKDIR /app

COPY --from=builder ./app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 4000
EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]
