# Build stage
FROM eclipse-temurin:25-jdk AS build

WORKDIR /backend

RUN apt-get update && apt-get install -y maven

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests


# Runtime stage
FROM eclipse-temurin:25-jdk

WORKDIR /backend

COPY --from=build /backend/target/*.jar bumfuzzle-backend.jar

CMD ["java","-jar","app.jar"]