# Usar OpenJDK 21 como imagen base
FROM openjdk:21-jdk-slim

# Información del mantenedor
LABEL maintainer="ditech-team@ditech.com"
LABEL description="Backend User Service - Spring Boot Application"

# Crear directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR generado por Maven
COPY target/*.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Variables de entorno
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=default

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Healthcheck para verificar que la aplicación esté funcionando
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1