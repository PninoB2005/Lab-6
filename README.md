# Miembros
Integrante 1: Oscar Daniel Aguas Quiroz Documento: 1102577572
Integrante 2: Juan Diego Mendoza Torres Documento: 1029720696
Integrante 3: Documento: 
Integrante 4: Documento: 
Integrante 5: Grupo: 


Laboratorio 6: Testing — Ingeniería de Software II, UNAL, 2026-II.
Proyecto Maven con Java 17 para el dominio de préstamo de equipos.

## Stack

| Pieza requerida | Herramienta | Función |
|---|---|---|
| Framework de pruebas | JUnit 5 (5.11.4) y Maven Surefire (3.5.2) | Ejecutar pruebas y reportar resultados. |
| Dobles de prueba | Mockito (5.14.2) | Configurar mocks y verificar interacciones. |
| Cliente HTTP y JSON | `java.net.http.HttpClient` (JDK 17) y Jackson Databind (2.18.2) | Realizar peticiones y convertir respuestas JSON. |
| Servidor HTTP de prueba | MockWebServer (4.12.0) | Responder peticiones HTTP reales en un servidor local de prueba. |
| Cobertura | JaCoCo (0.8.12) | Medir ejecución de líneas y ramas y generar un informe HTML. |

## Requisitos y ejecución

Instalar JDK 17 y Maven 3.9.x; comprobar `java -version` y `mvn -version`.
La primera ejecución requiere acceso a Maven Central para descargar dependencias.
Ejecutar desde la raíz del proyecto:

```sh
mvn test
mvn -Dtest=PrestamoTest test
mvn clean verify
```

El segundo comando se utiliza una vez incorporadas las pruebas de `Prestamo`.
Surefire genera informes en `target/surefire-reports/`.
JaCoCo genera el informe HTML en `target/site/jacoco/index.html` durante `verify`.

## Organización

- `src/main/java/domain`: reglas de negocio y contratos.
- `src/main/java/data`: adaptadores de infraestructura.
- `src/test/java`: pruebas automatizadas.

El dominio no depende de bases de datos, red ni frameworks.
