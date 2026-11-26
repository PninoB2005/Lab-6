package data;

import domain.Notificador;
import domain.Prestamo;
import domain.SolicitarPrestamo;
import fakes.PrestamoRepositoryEnMemoria;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolicitarPrestamoIntegrationTest {

    private MockWebServer servidor;
    private DisponibilidadEquiposHttp adaptador;
    private PrestamoRepositoryEnMemoria repositorio;
    private Notificador notificador;
    private SolicitarPrestamo casoDeUso;

    @BeforeEach
    void configurar() throws IOException {

        servidor = new MockWebServer();
        servidor.start();

        String urlBase = servidor.url("/").toString();

        adaptador = new DisponibilidadEquiposHttp(urlBase);

        repositorio = new PrestamoRepositoryEnMemoria();

        notificador = mock(Notificador.class);

        casoDeUso = new SolicitarPrestamo(
                adaptador,
                repositorio,
                notificador);
    }

    @AfterEach
    void limpiar() throws IOException {
        servidor.shutdown();
    }

    @Test
    void ejecutar_equipoDisponible_guardaPrestamoEnRepositorio()
            throws Exception {

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
        Prestamo prestamo = casoDeUso.ejecutar("1023", 12);

        // Assert
        assertNotNull(prestamo);

        assertEquals(1, repositorio.cantidadPrestamos());

        assertSame(
                prestamo,
                repositorio.obtenerTodos().get(0));

        assertEquals(1, prestamo.getId());
        assertEquals("1023", prestamo.getEstudianteId());
        assertEquals(12, prestamo.getEquipoId());

        verify(notificador, times(1)).notificar(
                "1023",
                "Solicitud 1 registrada");
    }
}
