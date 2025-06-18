# Etapa 1: Construcción
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final más liviana
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Puerto expuesto (opcional, útil para documentación)
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
