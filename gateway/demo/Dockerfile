FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copia o projeto inteiro
COPY . .

# Build sem testes
RUN mvn clean package -DskipTests

# Imagem final
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "service.jar"]