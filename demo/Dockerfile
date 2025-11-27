# Etapa 1: Build
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiamos wrapper y config de Maven
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

COPY pom.xml .

# Descargamos dependencias
RUN ./mvnw -q dependency:go-offline

# Copiamos el código fuente
COPY src src

# Construimos el JAR (sin tests para que sea más rápido)
RUN ./mvnw -q package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render pasa PORT; usamos 13131 por defecto para local
ENV PORT=13131
EXPOSE 13131

ENTRYPOINT ["java", "-jar", "app.jar"]
