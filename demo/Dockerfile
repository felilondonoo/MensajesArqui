# Build stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw -q dependency:go-offline

COPY src src

RUN ./mvnw -q package -DskipTests

# Run stage
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV PORT=13131
EXPOSE 13131

ENTRYPOINT ["java", "-jar", "app.jar"]
