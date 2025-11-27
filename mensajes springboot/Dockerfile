# Etapa 1: Build
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiamos wrapper y config de Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Descargamos dependencias
RUN ./mvnw -q dependency:go-offline

# Copiamos el código fuente
COPY src src

# Construimos el JAR (sin tests)
RUN ./mvnw -q package -DskipTests

# Etapa 2: Imagen final para correr la app
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Render pasará PORT como variable de entorno.
# No hace falta fijar un valor aquí, pero puedes dejar un default.
ENV PORT=13131

# Exponemos el puerto por claridad (opcional, pero ayuda)
EXPOSE 13131

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
