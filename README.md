# 🚀 Backend User Service - Spring Boot

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-20.10+-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

API REST para gestión de usuarios desarrollada con Spring Boot y Java 21, con monitoreo completo usando Prometheus y Grafana.

## 🔗 URLs de Acceso Rápido

Una vez que tengas la aplicación ejecutándose, puedes acceder a:

| Servicio | URL | Credenciales |
|----------|-----|-------------|
| 🌐 **Aplicación** | http://localhost:8080 | - |
| 📚 **Swagger UI** | http://localhost:8080/swagger-ui.html | - |
| 📊 **Métricas Raw** | http://localhost:8080/actuator/prometheus | - |
| 🔍 **Prometheus** | http://localhost:9090 | - |
| 📈 **Grafana** | http://localhost:3000 | `admin` / `admin` |
| 🗄️ **H2 Console** | http://localhost:8080/h2-console | `sa` / `password` |

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Ejecución del Proyecto](#-ejecución-del-proyecto)
- [API Endpoints](#-api-endpoints)
- [Base de Datos](#-base-de-datos)
- [Monitoreo y Métricas](#-monitoreo-y-métricas)
- [Testing](#-testing)
- [Docker](#-docker)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Configuración](#-configuración)
- [Troubleshooting](#-troubleshooting)

## ✨ Características

- 🔧 **API REST completa** para gestión de usuarios (CRUD)
- ☕ **Java 21** con Spring Boot 3.5.6
- 📊 **Monitoreo integrado** con Prometheus y Grafana
- 📚 **Documentación automática** con Swagger/OpenAPI
- 🗄️ **Base de datos H2** en memoria para desarrollo
- 🧪 **Testing completo** con JUnit 5 y Mockito
- 🐳 **Containerización** con Docker y Docker Compose
- 🔍 **Validación de datos** con Bean Validation
- 🎯 **Manejo de excepciones** centralizado
- 📈 **Métricas personalizadas** con Micrometer

## 🛠 Tecnologías

### Core
- **Java**: 21
- **Spring Boot**: 3.5.6
- **Maven**: 3.6+

### Dependencias Principales
- **Spring Boot Starter Web**: API REST
- **Spring Boot Starter Data JPA**: Persistencia de datos
- **Spring Boot Starter Actuator**: Métricas y health checks
- **Spring Boot Starter Validation**: Validación de datos
- **Flyway**: Migraciones de base de datos
- **H2 Database**: Base de datos en memoria
- **Lombok**: Reducción de código boilerplate
- **MapStruct**: Mapeo de objetos
- **SpringDoc OpenAPI**: Documentación de API
- **Micrometer Prometheus**: Métricas para Prometheus

### Herramientas de Desarrollo
- **JUnit 5**: Testing unitario
- **Mockito**: Mocking para tests
- **Docker**: Containerización
- **Prometheus**: Recolección de métricas
- **Grafana**: Visualización de métricas

## 📋 Requisitos Previos

### Para Desarrollo Local
- **Java JDK**: 21
- **Maven**: 3.6 o superior
- **Git**: Para clonar el repositorio

### Para Docker
- **Docker**: 20.10 o superior
- **Docker Compose**: 1.29 o superior

### Verificar Instalaciones
```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar Docker
docker --version
docker-compose --version
```

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/ditech-backend.git
cd ditech-backend/backend
```

### 2. Configurar Variables de Entorno (Opcional)
```bash
# Para desarrollo local
export JAVA_HOME=/path/to/java
export MAVEN_HOME=/path/to/maven

# Para Docker
export COMPOSE_PROJECT_NAME=ditech-backend
```

### 3. Instalar Dependencias
```bash
# Descargar dependencias
mvn dependency:resolve

# Limpiar y compilar
mvn clean compile
```

## 🏃‍♂️ Ejecución del Proyecto

### Opción 1: Ejecución Local con Maven

#### Desarrollo Rápido
```bash
# Ejecutar directamente con Java 21
mvn spring-boot:run

# Con perfil de desarrollo
mvn spring-boot:run -Dspring.profiles.active=dev
```

#### Compilar y Ejecutar JAR
```bash
# Compilar
mvn clean package

# Ejecutar JAR
java -jar target/backend-0.0.1-SNAPSHOT.jar

# Con opciones JVM personalizadas
java -Xmx512m -Xms256m -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Opción 2: Ejecución con Docker

#### Solo la Aplicación
```bash
# Compilar aplicación
mvn clean package 
o 
mvn clean compile package install se craran y ejecutaran solas en contenedor docker

# Construir imagen Docker
docker build -t ditech/backend-user-service .

# Ejecutar contenedor
docker run -p 8080:8080 ditech/backend-user-service
```

#### Stack Completo (App + Prometheus + Grafana)
```bash
# Compilar aplicación
mvn clean package

# Levantar todo el stack
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar servicios
docker-compose down
```

## 🌐 API Endpoints

### Base URL
- **Local**: `http://localhost:8080`
- **Docker**: `http://localhost:8080`

### Endpoints Principales

| Método | Endpoint | Descripción | Código de Respuesta |
|--------|----------|-------------|-------------------|
| `POST` | `/users` | Crear usuario | `201 Created` |
| `GET` | `/users` | Obtener todos los usuarios | `200 OK` |
| `GET` | `/users/{id}` | Obtener usuario por ID | `200 OK` / `404 Not Found` |
| `DELETE` | `/users/{id}` | Eliminar usuario | `204 No Content` / `404 Not Found` |

### Ejemplos de Uso

#### Crear Usuario
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john.doe@example.com",
    "active": true
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john.doe@example.com"
}
```

#### Obtener Todos los Usuarios
```bash
curl -X GET http://localhost:8080/users
```

#### Obtener Usuario por ID
```bash
curl -X GET http://localhost:8080/users/1
```

#### Eliminar Usuario
```bash
curl -X DELETE http://localhost:8080/users/1
```

### Validaciones de Datos

#### UserCreateRequestDto
- `username`: Obligatorio, 3-50 caracteres
- `email`: Obligatorio, formato de email válido
- `active`: Obligatorio, valor booleano

#### Respuestas de Error
```json
{
  "timestamp": "2024-01-20T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El nombre de usuario es obligatorio",
  "path": "/users"
}
```

## 🗄️ Base de Datos

### Configuración H2 (Desarrollo)
- **URL**: `jdbc:h2:mem:userdb`
- **Usuario**: `sa`
- **Contraseña**: `password`
- **Consola H2**: http://localhost:8080/h2-console

### Estructura de la Tabla `users`
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Migraciones con Flyway
Las migraciones se encuentran en `src/main/resources/db/migration/`:
- `V1__Create_user_table.sql`: Creación de tabla users e índices

## 📊 Monitoreo y Métricas

### Endpoints de Actuator
| Endpoint | Descripción | URL |
|----------|-------------|-----|
| Health Check | Estado de la aplicación | http://localhost:8080/actuator/health |
| Info | Información de la aplicación | http://localhost:8080/actuator/info |
| Métricas | Métricas de la aplicación | http://localhost:8080/actuator/metrics |
| Prometheus | Métricas para Prometheus | http://localhost:8080/actuator/prometheus |

### Prometheus
- **URL**: http://localhost:9090
- **Configuración**: `monitoring/prometheus.yml`
- **Targets**: Aplicación Spring Boot en puerto 8080

### Grafana
- **URL**: http://localhost:3000
- **Credenciales**: `admin` / `admin`
- **Datasource**: Prometheus configurado automáticamente
- **Dashboards**: Precargados en `monitoring/grafana/dashboards/`

### Métricas Principales
- `http_server_requests_seconds`: Latencia de requests HTTP
- `jvm_memory_used_bytes`: Uso de memoria JVM
- `system_cpu_usage`: Uso de CPU del sistema
- `users_created_total`: Contador personalizado de usuarios creados

## 🧪 Testing

### Ejecutar Tests
```bash
# Todos los tests
mvn test

# Tests específicos
mvn test -Dtest=UserControllerTest
mvn test -Dtest=UserServiceTest

# Tests con reporte de cobertura
mvn test jacoco:report
```

### Estructura de Tests
```
src/test/java/com/ditech/backend/
├── BackendUserServiceApplicationTests.java    # Test de contexto
├── controller/
│   └── UserControllerTest.java                # Tests de controlador
└── service/
    └── UserServiceTest.java                   # Tests de servicio
```

### Tipos de Tests
- **Tests Unitarios**: Servicios y componentes individuales
- **Tests de Integración**: Controladores con MockMvc
- **Tests de Contexto**: Carga completa de la aplicación

### Ejemplo de Ejecución
```bash
# Resultado esperado
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## 🐳 Docker

### Dockerfile
La aplicación usa **Java 21** para máximo rendimiento:
```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose
El archivo `docker-compose.yml` incluye:
- **backend-user-service**: Tu aplicación (puerto 8080)
- **prometheus**: Recolección de métricas (puerto 9090)
- **grafana**: Visualización (puerto 3000)

### Comandos Docker Útiles
```bash
# Ver contenedores en ejecución
docker-compose ps

# Ver logs de un servicio específico
docker-compose logs -f backend-user-service

# Reiniciar un servicio
docker-compose restart backend-user-service

# Acceder al contenedor
docker-compose exec backend-user-service bash

# Limpiar todo
docker-compose down -v --rmi all
```

## 📁 Estructura del Proyecto

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/ditech/backend/
│   │   │   ├── BackendUserServiceApplication.java
│   │   │   ├── config/
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/
│   │   │   │   └── UserController.java
│   │   │   ├── dto/
│   │   │   │   ├── UserCreateRequestDto.java
│   │   │   │   └── UserResponseDto.java
│   │   │   ├── exception/
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── UserNotFoundException.java
│   │   │   ├── mapper/
│   │   │   │   └── UserMapper.java
│   │   │   ├── model/
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java
│   │   │   └── service/
│   │   │       └── UserService.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/
│   │           └── V1__Create_user_table.sql
│   └── test/
│       └── java/com/ditech/backend/
│           ├── BackendUserServiceApplicationTests.java
│           ├── controller/
│           │   └── UserControllerTest.java
│           └── service/
│               └── UserServiceTest.java
├── monitoring/
│   ├── grafana/
│   │   ├── dashboards/
│   │   └── provisioning/
│   └── prometheus.yml
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## ⚙️ Configuración

### Perfiles de Maven
El proyecto está configurado para **Java 21** con Spring Boot 3.5.6:

- **Spring Boot**: 3.5.6
- **SpringDoc OpenAPI**: 2.8.13
- **Java Version**: 21


### Variables de Entorno
```bash
# Configuración de la aplicación
SPRING_PROFILES_ACTIVE=default
JAVA_OPTS=-Xmx512m -Xms256m

# Base de datos H2 (configuración por defecto)
SPRING_DATASOURCE_URL=jdbc:h2:mem:userdb
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=password


# Configuración de Grafana
GF_SECURITY_ADMIN_PASSWORD=admin
```

### Puertos Utilizados
- **8080**: Aplicación Spring Boot
- **9090**: Prometheus
- **3000**: Grafana

## 📚 Documentación de API

### Swagger UI
- **URL**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Características de Swagger
- Documentación interactiva de todos los endpoints
- Posibilidad de probar endpoints directamente
- Esquemas de datos detallados
- Códigos de respuesta y ejemplos

## 🔧 Troubleshooting

### Problemas Comunes

#### Error de Puerto en Uso
```bash
# Verificar qué proceso usa el puerto 8080
netstat -tulpn | grep 8080

# Matar proceso si es necesario
kill -9 <PID>
```

#### Error de Versión de Java
```bash
# Verificar versión de Java (debe ser 21)
java -version

# Verificar JAVA_HOME
echo $JAVA_HOME

# Compilar con Java 21
mvn clean package
```

#### Error de Memoria en Docker
```bash
# Aumentar memoria disponible para Docker
docker run -m 1g -p 8080:8080 ditech/backend-user-service
```

#### Base de Datos H2 no Accesible
1. Verificar que la aplicación esté ejecutándose
2. Acceder a: http://localhost:8080/h2-console
3. Usar configuración:
   - JDBC URL: `jdbc:h2:mem:userdb`
   - User: `sa`
   - Password: `password`

#### Prometheus no Recolecta Métricas
1. Verificar que la aplicación exponga métricas: http://localhost:8080/actuator/prometheus
2. Verificar configuración en `monitoring/prometheus.yml`
3. Verificar que Prometheus pueda acceder a la aplicación

### Logs Útiles
```bash
# Ver logs de la aplicación
docker-compose logs -f backend-user-service

# Ver logs de Prometheus
docker-compose logs -f prometheus

# Ver logs de Grafana
docker-compose logs -f grafana
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👥 Equipo

- Bryant Alfonso

## 🔗 Enlaces Útiles

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Docker Documentation](https://docs.docker.com/)
- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Documentation](https://grafana.com/docs/)
- [H2 Database Documentation](http://www.h2database.com/html/main.html)

---

⭐ **¡Si este proyecto te fue útil, no olvides darle una estrella!** ⭐
