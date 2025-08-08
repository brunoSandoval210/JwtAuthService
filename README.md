# 🛡️ JWTAuthService

**JWTAuthService** es un proyecto backend desarrollado con **Spring Boot 3** y **Java 21** que implementa un sistema de autenticación y autorización segura mediante **JWT (JSON Web Tokens)** y **Spring Security**.  

Incluye funcionalidades de **registro de usuarios, inicio de sesión, CRUD de usuarios autenticados**, documentación interactiva con **Swagger**, y un **manejador global de excepciones personalizadas** usando `@RestControllerAdvice`.

---

## 🚀 Características principales

- 🔐 **Autenticación segura con JWT**.
- 👥 Registro, login y gestión de usuarios.
- 🛡️ Endpoints públicos y protegidos con control de acceso.
- 📚 Documentación interactiva con **Swagger UI**.
- ⚠️ Manejo global de excepciones personalizadas con `@RestControllerAdvice`.
- 🗄️ Persistencia con **PostgreSQL** y **Spring Data JPA**.
- 🏗️ Arquitectura en capas (Controller, Service, Repository, DTO).

---

## 📦 Tecnologías utilizadas

- **Java 21**
- **Spring Boot 3**
- **Spring Security**
- **JWT (jjwt / java-jwt)**
- **Spring Data JPA**
- **PostgreSQL**
- **Swagger / OpenAPI**
- **Lombok**

---

## 📋 Requisitos previos

Antes de iniciar, asegúrate de tener instalado:

- [Java 21](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/)
- [Git](https://git-scm.com/)

---

## ⚙️ Configuración de variables de entorno

El proyecto utiliza variables de entorno para la configuración sensible. Debes definir las siguientes antes de iniciar la aplicación:

| Variable             | Descripción                                           | Ejemplo                                   |
|----------------------|-------------------------------------------------------|-------------------------------------------|
| `DATABASE_URL`       | URL de conexión JDBC a PostgreSQL                     | `jdbc:postgresql://localhost:5432/jwtdb` |
| `DATABASE_USERNAME`  | Usuario de la base de datos                           | `postgres`                               |
| `DATABASE_PASSWORD`  | Contraseña de la base de datos                        | `admin123`                               |
| `PRIVATE_KEY`        | Clave privada para firmar los JWT                     | `qFCq4CWUwOK25mN/IwiORnoMXBcCR6TfBzlX7Y+YOoo=` |
| `JWT_USER_GENERATOR` | Identificador del generador de tokens (issuer)        | `JwtAuthService`                         |
| `JWT_EXPIRE`         | Tiempo de expiración del token en milisegundos        | `3600000`                                |


## Ejemplo en Linux/Mac:

export DATABASE_URL=jdbc:postgresql://localhost:5432/jwtdb
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=admin123
export PRIVATE_KEY=qFCq4CWUwOK25mN/IwiORnoMXBcCR6TfBzlX7Y+YOoo=
export JWT_USER_GENERATOR=JwtAuthService
export JWT_EXPIRE=3600000

##Ejemplo en Windows (PowerShell):
setx DATABASE_URL "jdbc:postgresql://localhost:5432/jwtdb"
setx DATABASE_USERNAME "postgres"
setx DATABASE_PASSWORD "admin123"
setx PRIVATE_KEY "qFCq4CWUwOK25mN/IwiORnoMXBcCR6TfBzlX7Y+YOoo="
setx JWT_USER_GENERATOR "JwtAuthService"
setx JWT_EXPIRE "3600000"

---

## 🛠️ Instalación y ejecución

## Clonar el repositorio

- git clone https://github.com/tuusuario/JWTAuthService.git
- cd JWTAuthService
- Configurar la base de datos

## Crea una base de datos en PostgreSQL:

CREATE DATABASE jwtdb;
Asegúrate de que el usuario y contraseña coincidan con las variables de entorno.

## Configurar variables de entorno

Define las variables indicadas en la sección anterior.

## Compilar y ejecutar el proyecto

mvn clean install
mvn spring-boot:run

---

## Acceder a la documentación de la API

Swagger UI estará disponible en:
👉 http://localhost:8080/swagger-ui.html
o
👉 http://localhost:8080/swagger-ui/index.html

---

## 👥 Usuarios de prueba
La base de datos se inicializa con dos usuarios para realizar pruebas de autenticación:

BrunoUser	1234	ROLE_USER
BrunoAdmin	1234	ROLE_ADMIN

Estos usuarios pueden utilizarse para probar el flujo de login (/auth/login) y acceder a los endpoints protegidos según sus permisos.

---
## 🛡️ Manejo de excepciones
El proyecto implementa un manejador global de excepciones personalizadas usando @RestControllerAdvice, lo que asegura respuestas consistentes y mensajes claros en errores de validación, autenticación o reglas de negocio.
