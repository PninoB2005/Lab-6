# Miembros

Integrante 1: Oscar Daniel Aguas Quiroz Documento: 1102577572

Integrante 2: Juan Diego Mendoza Torres Documento: 1029720696

Integrante 3: Sergio Tovar Vasquez Documento 1027280991:

Integrante 4: Pablo Andres Niño Barreto Documento 1052382006:

---

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

---

## Parte 1 - Pruebas unitarias del dominio

Esta parte se enfoca en verificar las reglas de negocio del agregado `Prestamo` mediante pruebas unitarias, sin depender de conexiones de red, bases de datos ni frameworks externos.

### Entidad Prestamo

Se implementó la entidad `Prestamo`, que representa una solicitud de préstamo de equipos y contiene los siguientes atributos:

- `id`: identificador del préstamo.
- `estudianteId`: identificador del estudiante.
- `equipoId`: identificador del equipo.
- `estado`: estado actual del préstamo.
- `fechaLimite`: fecha límite de devolución.
- `renovaciones`: cantidad de renovaciones realizadas.

### Estados del préstamo

Se implementó el enumerado `EstadoPrestamo` con los siguientes estados:

- `PENDIENTE`
- `APROBADO`
- `DEVUELTO`

### Reglas de negocio

| Operación | Condición | Comportamiento |
|---|---|---|
| `aprobar(hoy)` | El préstamo está pendiente. | Cambia el estado a aprobado y establece una fecha límite de siete días. |
| `renovar()` | El préstamo está aprobado y no ha sido renovado. | Extiende la fecha límite siete días e incrementa el contador de renovaciones. |
| `devolver()` | El préstamo está aprobado. | Cambia el estado a devuelto. |

Se implementaron siete escenarios de prueba para verificar los comportamientos correctos y los errores correspondientes a cada operación.

Las pruebas utilizan la estructura AAA:

- Arrange: preparación del escenario.
- Act: ejecución de la operación.
- Assert: verificación del resultado.

También se realizó una modificación intencional de una regla de negocio para comprobar que las pruebas detectaran el error, restaurando posteriormente la implementación original.

## Parte 2 - Desarrollo guiado por pruebas (TDD)

Esta parte consiste en implementar la clase `CalculadoraMulta` utilizando la metodología Test-Driven Development (TDD).

Se trabaja mediante tres etapas:

1. **Rojo:** escribir una prueba que inicialmente falle.
2. **Verde:** implementar el código mínimo necesario para superar la prueba.
3. **Refactor:** mejorar la implementación manteniendo todas las pruebas aprobadas.

### Reglas de cálculo

La operación `calcular(fechaLimite, fechaDevolucion, esMonitor)` determina el valor de la multa de acuerdo con las siguientes reglas:

| Condición | Resultado |
|---|---|
| Devolución en la fecha límite o antes | $0 |
| Cada día de retraso | $2.000 |
| Multa máxima | $50.000 |
| Estudiante monitor | Paga la mitad de la multa después de aplicar el tope. |

### Ciclos de TDD

Se desarrollaron cuatro ciclos de implementación, incorporando progresivamente cada regla de negocio.

Se verificaron los siguientes escenarios:

- Devolución antes de la fecha límite.
- Devolución exactamente en la fecha límite.
- Un día de retraso.
- Veinticinco días de retraso.
- Cuarenta días de retraso.
- Estudiante monitor con tres días de retraso.

Se utilizaron operaciones de fechas de Java para calcular los días de retraso y constantes para evitar números mágicos en la implementación.

## Parte 3 - Dobles de prueba

Esta parte tiene como objetivo verificar el comportamiento del caso de uso `SolicitarPrestamo` sin depender de una base de datos real ni de servicios externos.

Para ello, se definieron tres contratos mediante interfaces:

| Contrato | Operación | Responsabilidad |
|---|---|---|
| `DisponibilidadEquipos` | `estaDisponible(equipoId)` | Consultar la disponibilidad de un equipo. |
| `PrestamoRepository` | `siguienteId()` y `guardar(prestamo)` | Obtener identificadores y almacenar préstamos. |
| `Notificador` | `notificar(estudianteId, mensaje)` | Enviar notificaciones al estudiante. |

También se implementó la excepción `EquipoNoDisponible`.

### Caso de uso SolicitarPrestamo

El caso de uso recibe sus dependencias mediante inyección por constructor.

Su funcionamiento consiste en:

1. Consultar si el equipo está disponible.
2. Si no está disponible, lanzar `EquipoNoDisponible`.
3. Obtener un identificador mediante el repositorio.
4. Crear el préstamo.
5. Guardar el préstamo en el repositorio.
6. Notificar al estudiante.
7. Retornar el préstamo creado.

### Escenarios de prueba

Se implementaron tres pruebas utilizando Mockito:

- **Equipo disponible:** verifica que el préstamo se cree, se almacene y se envíe la notificación correspondiente.
- **Equipo no disponible:** verifica que se lance la excepción y que no se guarde ni notifique el préstamo.
- **Fallo al guardar:** verifica que el error del repositorio se propague y que no se envíe la notificación.

Estas pruebas permiten comprobar tanto los resultados como las interacciones entre los componentes del caso de uso.

## Parte 4 - Pruebas de integración HTTP

Esta parte se enfoca en verificar la integración entre el cliente HTTP, la conversión de JSON, el adaptador y un servidor HTTP local de prueba.

### Adaptador HTTP

Se implementó la clase `DisponibilidadEquiposHttp`, que implementa el contrato `DisponibilidadEquipos` y permite consultar la disponibilidad de los equipos mediante una API REST.

El adaptador recibe la URL base mediante su constructor, permitiendo utilizar diferentes direcciones para el servicio y el servidor de pruebas.

Se utilizaron las siguientes herramientas:

- `HttpClient`: cliente HTTP incluido en Java 17 para realizar las solicitudes.
- Jackson: biblioteca para procesar y deserializar las respuestas JSON.
- MockWebServer: servidor HTTP local para simular las respuestas del servicio.

### Petición HTTP

El adaptador realiza una solicitud GET con la siguiente estructura:

```http
GET <urlBase>/api/v1/equipos/<id>
```

Ejemplo de respuesta JSON:

```json
{
  "id": 12,
  "nombre": "Portatil",
  "estado": "disponible"
}
```

### Manejo de respuestas HTTP

| Respuesta | Comportamiento |
|---|---|
| HTTP 200 con estado disponible | Retorna `true`. |
| HTTP 200 con cualquier otro estado | Retorna `false`. |
| HTTP 404 | Retorna `false`. |
| Otro código HTTP | Lanza `ServicioNoDisponible` con el código correspondiente. |
| Error de red | Lanza `ServicioNoDisponible` con el mensaje del error. |

### Pruebas de integración HTTP

Se implementaron cuatro escenarios utilizando MockWebServer:

1. **Equipo disponible:** verifica que el adaptador retorne `true`, utilice el método GET y construya correctamente la ruta `/api/v1/equipos/12`.
2. **Equipo prestado:** verifica que una respuesta HTTP 200 con estado `prestado` retorne `false`.
3. **Equipo inexistente:** verifica que una respuesta HTTP 404 retorne `false`.
4. **Error del servidor:** verifica que una respuesta HTTP 500 genere `ServicioNoDisponible` con el mensaje `HTTP 500`.

### Integración de extremo a extremo

Se implementó una prueba que integra los siguientes componentes:

- `SolicitarPrestamo`: caso de uso real.
- `DisponibilidadEquiposHttp`: adaptador HTTP real.
- `MockWebServer`: servidor HTTP local de pruebas.
- `PrestamoRepositoryEnMemoria`: implementación fake del repositorio.
- `Notificador`: mock utilizado para verificar las notificaciones.

La prueba verifica que, cuando el servicio HTTP indica que el equipo está disponible, se crea el préstamo y queda almacenado correctamente en el repositorio en memoria.

### Fake del repositorio

Se implementó `PrestamoRepositoryEnMemoria`, que utiliza una lista para almacenar los préstamos y un contador para generar identificadores consecutivos.

Esto permite comprobar el almacenamiento real de los préstamos durante las pruebas sin utilizar una base de datos.

## Parte 5 - Cobertura y calidad de las pruebas

Esta parte busca evaluar qué proporción del código está siendo ejecutada por las pruebas y comprobar si estas son capaces de detectar errores introducidos intencionalmente.

### Medición de cobertura

Se utiliza JaCoCo para generar informes de cobertura de líneas y ramas del proyecto.

Las actividades contempladas son:

1. Ejecutar todas las pruebas utilizando la herramienta de cobertura.
2. Revisar los porcentajes de cobertura de los paquetes `domain` y `data`.
3. Identificar las líneas de código que no están siendo ejecutadas.
4. Agregar pruebas adicionales cuando sea necesario.
5. Generar el reporte HTML de cobertura.

Las metas establecidas por el laboratorio son:

- Al menos 90 % de cobertura de líneas en `domain`.
- Al menos 80 % de cobertura de líneas en `data`.

### Pruebas de mutación manual

Se deben realizar modificaciones intencionales en el código para comprobar si las pruebas detectan los errores.

Los mutantes establecidos son:

- Modificar la condición de renovaciones de `renovaciones < 1` a `renovaciones < 2`.
- Cambiar el límite máximo de la multa de $50.000 a $60.000.
- Cambiar la condición de respuesta HTTP 404 a HTTP 400.

Después de cada modificación se ejecutan las pruebas y se registra si el mutante fue detectado o sobrevivió. Finalmente, se restaura el código original.

Los resultados definitivos de cobertura y mutación se documentarán después de completar las actividades correspondientes.

---

## Ejecución de pruebas por parte

### Ejecutar todas las pruebas

```bash
mvn clean test
```

### Parte 1 - Pruebas del préstamo

```bash
mvn -Dtest=PrestamoTest test
```

### Parte 2 - Pruebas de la calculadora de multas

```bash
mvn -Dtest=CalculadoraMultaTest test
```

### Parte 3 - Pruebas del caso de uso

```bash
mvn -Dtest=SolicitarPrestamoTest test
```

### Parte 4 - Pruebas del adaptador HTTP

```bash
mvn -Dtest=DisponibilidadEquiposHttpTest test
```

### Parte 4 - Prueba de extremo a extremo

```bash
mvn -Dtest=SolicitarPrestamoIntegrationTest test
```

### Parte 4 - Todas las pruebas de integración

```bash
mvn "-Dtest=DisponibilidadEquiposHttpTest,SolicitarPrestamoIntegrationTest" test
```

### Generar el reporte de cobertura

```bash
mvn clean verify
```

El reporte HTML generado por JaCoCo se encuentra en:

```text
target/site/jacoco/index.html
```

Los informes de ejecución de JUnit se encuentran en:

```text
target/surefire-reports/
```

## Evidencias del laboratorio

La entrega del laboratorio se organiza mediante las evidencias E1 a E20 establecidas en la guía:

| Parte | Evidencias |
|---|---|
| Preparación del proyecto y stack | E1 – E2 |
| Parte 1: Pruebas unitarias del dominio | E3 – E5 |
| Parte 2: Desarrollo guiado por pruebas (TDD) | E6 – E9 |
| Parte 3: Dobles de prueba | E10 – E12 |
| Parte 4: Integración HTTP | E13 – E16 |
| Parte 5: Cobertura y calidad | E17 – E20 |

Las evidencias se presentan en un único documento PDF grupal, organizadas y numeradas según los requisitos del laboratorio.

---

**Universidad Nacional de Colombia — Sede Bogotá**

Ingeniería de Sistemas y Computación

Ingeniería de Software II — 2026-II