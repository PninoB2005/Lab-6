package data;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DataAdditionalTest {

    private MockWebServer servidor;
    private DisponibilidadEquiposHttp adaptador;

    @BeforeEach
    void configurar() throws IOException {
        servidor = new MockWebServer();
        servidor.start();
        adaptador = new DisponibilidadEquiposHttp(servidor.url("/").toString());
    }

    @AfterEach
    void limpiar() throws IOException {
        servidor.shutdown();
    }

    @Test
    void estaDisponible_respuestaConJsonInvalido_lanzaServicioNoDisponible() {
        servidor.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("{ esto no es json valido"));

        ServicioNoDisponible ex = assertThrows(
                ServicioNoDisponible.class,
                () -> adaptador.estaDisponible(12));

        assertNotNull(ex.getCause());
    }
}
