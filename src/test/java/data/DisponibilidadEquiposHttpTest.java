package data;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class DisponibilidadEquiposHttpTest {

    private MockWebServer servidor;
    private DisponibilidadEquiposHttp adaptador;

    @BeforeEach
    void configurar() throws IOException {
        servidor = new MockWebServer();
        servidor.start();

        String urlBase = servidor.url("/").toString();

        adaptador = new DisponibilidadEquiposHttp(urlBase);
    }

    @AfterEach
    void limpiar() throws IOException {
        servidor.shutdown();
    }

    @Test
    void estaDisponible_equipoDisponible_devuelveTrueYLlamaRutaCorrecta()
            throws InterruptedException {

        // Arrange
        servidor.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                        {
                            "id": 12,
                            "nombre": "Portatil",
                            "estado": "disponible"
                        }
                        """));

        // Act
        boolean resultado = adaptador.estaDisponible(12);

        // Assert
        assertTrue(resultado);

        RecordedRequest peticion = servidor.takeRequest(1, TimeUnit.SECONDS);

        assertNotNull(peticion);
        assertEquals("GET", peticion.getMethod());
        assertEquals("/api/v1/equipos/12", peticion.getPath());
    }

    @Test
    void estaDisponible_equipoPrestado_devuelveFalse() {

        // Arrange
        servidor.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                        {
                            "id": 12,
                            "nombre": "Portatil",
                            "estado": "prestado"
                        }
                        """));

        // Act
        boolean resultado = adaptador.estaDisponible(12);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void estaDisponible_equipoInexistente_devuelveFalse() {

        // Arrange
        servidor.enqueue(new MockResponse()
                .setResponseCode(404));

        // Act
        boolean resultado = adaptador.estaDisponible(99);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void estaDisponible_errorServidor_lanzaServicioNoDisponible() {

        // Arrange
        servidor.enqueue(new MockResponse()
                .setResponseCode(500));

        // Act
        ServicioNoDisponible excepcion = assertThrows(
                ServicioNoDisponible.class,
                () -> adaptador.estaDisponible(12));

        // Assert
        assertEquals("HTTP 500", excepcion.getMessage());
    }
}
