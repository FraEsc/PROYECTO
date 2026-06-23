GymFit - Sistema de Gestión de Gimnasio
1. Descripción del proyecto
GymFit es un sistema backend desarrollado con microservicios para administrar un gimnasio. Permite gestionar socios, aviones, membresías, entrenadores, clases grupales y reservas.

El sistema está compuesto por dos microservicios principales y un API Gateway:

gym-socios-api
gym-reservas-api
gym-gateway-api
Cada microservicio trabaja con arquitectura por capas: Controlador, Servicio, Repositorio y Modelo.

2. Integrantes
Camila
Francisca
3. Aporte de cada integrante
Camila
Desarrollo de gym-socios-api.
Creación de modelos Socio, Plany Membresia.
Implementación de CRUD, DTOs, validaciones, logs y pruebas unitarias.
Francisca
Desarrollo de gym-reservas-api.
Creación de modelos Entrenador, ClaseGrupaly Reserva.
Implementación de Feign Client, API Gateway, Swagger y pruebas unitarias.
4. Microservicios
API de gimnasio-socios
Puerto:

8081
Base de datos:

db_gym_socios
Funciones:

Gestión de socios.
Gestión de planes.
Gestión de socios.
Validación de membresía activa.
API de reservas de gimnasio
Puerto:

8082
Base de datos:

db_gym_reservas
Funciones:

Gestión de entrenadores.
Gestión de clases grupales.
Gestión de reservas.
Comunicación con gym-socios-apipara validar membresías.
API de puerta de enlace de gimnasio
Puerto:

8080
Función:

Centralizar las solicitudes y redirigirlas a los microservicios correspondientes.
5. Comunicación entre microservicios
El microservicio gym-reservas-apise comunica con gym-socios-apimediante Feign Client.

Cuando se crea una reserva, el sistema valida:

Que la clase exista.
Que tenga cupos disponibles.
Que el socio tenga membresía activa.
Si alguna condición no se cumple, la reserva no se crea.

6. Puntos finales principales
Socios
GET    /api/v1/socios
GET    /api/v1/socios/{id}
POST   /api/v1/socios
PUT    /api/v1/socios/{id}
DELETE /api/v1/socios/{id}
Aviones
GET    /api/v1/planes
GET    /api/v1/planes/{id}
POST   /api/v1/planes
PUT    /api/v1/planes/{id}
DELETE /api/v1/planes/{id}
Membresías
GET    /api/v1/membresias
GET    /api/v1/membresias/{id}
POST   /api/v1/membresias
PUT    /api/v1/membresias/{id}
DELETE /api/v1/membresias/{id}
GET    /api/v1/membresias/socio/{idSocio}/activa
Entrenadores
GET    /api/v1/entrenadores
GET    /api/v1/entrenadores/{id}
POST   /api/v1/entrenadores
PUT    /api/v1/entrenadores/{id}
DELETE /api/v1/entrenadores/{id}
Clases
GET    /api/v1/clases
GET    /api/v1/clases/{id}
POST   /api/v1/clases
PUT    /api/v1/clases/{id}
DELETE /api/v1/clases/{id}
Reservas
GET    /api/v1/reservas
GET    /api/v1/reservas/{id}
POST   /api/v1/reservas
PUT    /api/v1/reservas/{id}
DELETE /api/v1/reservas/{id}
GET    /api/v1/reservas/socio/{idSocio}
7. Rutas por API Gateway
El Gateway funciona en:

http://localhost:8080
Rutas principales:

http://localhost:8080/api/v1/socios
http://localhost:8080/api/v1/planes
http://localhost:8080/api/v1/membresias
http://localhost:8080/api/v1/entrenadores
http://localhost:8080/api/v1/clases
http://localhost:8080/api/v1/reservas
8. Presumir
Socios de Swagger:

http://localhost:8081/swagger-ui/index.html
Swagger reservas:

http://localhost:8082/swagger-ui/index.html
9. Base de datos
El sistema utiliza MySQL.

Crear las bases de datos:

CREATE DATABASE db_gym_socios;
CREATE DATABASE db_gym_reservas;
10. Instrucciones de ejecución
Iniciar MySQL desde Laragon.
Crear las bases de datos.
Ejecutar gym-socios-api.
Ejecutar gym-reservas-api.
Ejecutar gym-gateway-api.
Probar los puntos finales desde Postman o Swagger.
Puertos:

gym-socios-api     → 8081
gym-reservas-api   → 8082
gym-gateway-api    → 8080
11. Pruebas unitarias
El proyecto incluye pruebas unitarias en la capa Service.

Pruebas implementadas:

SocioServiceTest
ReservaServiceTest
Casos probados:

Registro correcto de socio.
Validación de RUT duplicado.
Validación de correo duplicado.
Reserva sin cupos disponibles.
Reserva con socio sin membresía activa.
Reserva correcta con datos válidos.
12. Tecnologías utilizadas
Java 17
Bota de primavera
Spring Web
Spring Data JPA
Hibernar
MySQL
Lombok
Validación de beans
OpenFingir
Puerta de enlace de Spring Cloud
Swagger / OpenAPI
JUnit
Mockito
Experto
Cartero
