FROM maven:3.9.5-eclipse-temurin-21 as builder

WORKDIR /order-service

COPY pom.xml ./
RUN mvn dependency:resolve

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /order-service

COPY --from=builder /order-service/target/*.jar order-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "order-service.jar"]