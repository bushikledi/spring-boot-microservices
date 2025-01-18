FROM maven:3.9.5-eclipse-temurin-21 as builder

WORKDIR /stock-service

COPY pom.xml ./
RUN mvn dependency:resolve

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /stock-service

COPY --from=builder /stock-service/target/*.jar stock-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "stock-service.jar"]
