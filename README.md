# 🛡️ JWTAuthService - Sistema de Autenticación JWT con Spring Boot

**JWTAuthService** es un proyecto backend desarrollado con **Spring Boot 3** y **Java 21** que implementa un sistema seguro de autenticación y autorización mediante **JWT** y **Spring Security 6**.

![Java](https://img.shields.io/badge/Java-21-%23ED8B00?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-%236DB33F?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-%23316192?logo=postgresql)

## 🌟 Características Principales

- 🔐 Autenticación segura con **JWT** (tokens de acceso)
- 👨‍💻 Gestión de usuarios con roles (**USER**, **ADMIN**)
- 🛡️ Endpoints protegidos con `@PreAuthorize`
- 📊 Documentación interactiva con **Swagger UI**
- ⚠️ Manejo global de excepciones con `@RestControllerAdvice`
- 🗃️ Persistencia con **PostgreSQL** y **Spring Data JPA**
- 🏗️ Arquitectura en capas (Controller-Service-Repository)

## 🛠️ Tecnologías Utilizadas

| Categoría       | Tecnologías                                                                 |
|-----------------|-----------------------------------------------------------------------------|
| **Backend**     | Spring Boot 3, Spring Security 6, Spring Data JPA                           |
| **Base Datos**  | PostgreSQL 15                                                               |
| **Seguridad**   | JWT (jjwt), BCryptPasswordEncoder                                          |
| **Herramientas**| Lombok, MapStruct, OpenAPI 3, Swagger UI                                   |

## 📋 Requisitos Previos

- [JDK 21](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [PostgreSQL 15+](https://www.postgresql.org/)
- [Git](https://git-scm.com/)

## ⚙️ Configuración de Variables de Entorno

El proyecto requiere las siguientes variables:

| Variable             | Descripción                                | Ejemplo                                   |
|----------------------|--------------------------------------------|-------------------------------------------|
| `DATABASE_URL`       | URL de conexión JDBC                       | `jdbc:postgresql://localhost:5432/jwtdb` |
| `DATABASE_USERNAME`  | Usuario de PostgreSQL                      | `postgres`                               |
| `DATABASE_PASSWORD`  | Contraseña de PostgreSQL                   | `admin123`                               |
| `PRIVATE_KEY`        | Clave secreta para JWT (Base64)            | `qFCq4CWUwOK25mN/IwiORnoMXBcCR6TfBzlX7Y+YOoo=` |
| `JWT_USER_GENERATOR` | Emisor del token                           | `JwtAuthService`                         |
| `JWT_EXPIRE`         | Expiración del token (ms)                  | `3600000` (1 hora)                       |

### Configuración por Sistema Operativo

**Linux/Mac:**
```
export DATABASE_URL=jdbc:postgresql://localhost:5432/jwtdb
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=admin123
export PRIVATE_KEY=qFCq4CWUwOK25mN/IwiORnoMXBcCR6TfBzlX7Y+YOoo=
export JWT_USER_GENERATOR=JwtAuthService
export JWT_EXPIRE=3600000
```

**Ejemplo en Windows (PowerShell):**

```
setx DATABASE_URL "jdbc:postgresql://localhost:5432/jwtdb"
setx DATABASE_USERNAME "postgres"
setx DATABASE_PASSWORD "admin123"
setx PRIVATE_KEY "qFCq4CWUwOK25mN/IwiORnoMXBcCR6TfBzlX7Y+YOoo="
setx JWT_USER_GENERATOR "JwtAuthService"
setx JWT_EXPIRE "3600000"
```

---

## 🛠️ Instalación y ejecución

**Clonar el repositorio**
```
- git clone https://github.com/tuusuario/JWTAuthService.git
- cd JWTAuthService
- Configurar la base de datos
```

**Crea una base de datos en PostgreSQL:**

```
CREATE DATABASE jwtdb;
Asegúrate de que el usuario y contraseña coincidan con las variables de entorno.
```
**Configurar variables de entorno**
```
Define las variables indicadas en la sección anterior.
```
**Compilar y ejecutar el proyecto**
```
mvn clean install
mvn spring-boot:run
```
---

## Acceder a la documentación de la API

Swagger UI estará disponible en:
👉 http://localhost:8080/swagger-ui.html
o
👉 http://localhost:8080/swagger-ui/index.html

---

## 👥 Usuarios de prueba
La base de datos se inicializa con dos usuarios para realizar pruebas de autenticación:
```
BrunoUser	1234	ROLE_USER
BrunoAdmin	1234	ROLE_ADMIN
```

Estos usuarios pueden utilizarse para probar el flujo de login (/auth/login) y acceder a los endpoints protegidos según sus permisos.

---
## 🛡️ Manejo de excepciones
El proyecto implementa un manejador global de excepciones personalizadas usando @RestControllerAdvice, lo que asegura respuestas consistentes y mensajes claros en errores de validación, autenticación o reglas de negocio.
