# Utilizar una imagen base de Java que incluya el JDK
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR compilado al contenedor
# Asume que ya has construido tu aplicación con Maven o Gradle
# por ejemplo, usando el comando: ./mvnw package
COPY build/libs/*.jar app.jar

# Exponer el puerto en el que se ejecuta la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]