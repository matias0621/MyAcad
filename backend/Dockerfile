# Etapa 1: build con Maven y JDK 21
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiar pom.xml y el wrapper primero (para cachear dependencias)
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente y compilar
COPY src src
RUN ./mvnw clean package -DskipTests

# Etapa 2: imagen final ligera con JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
