package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolicitarPrestamoTest {

    @Test
    void ejecutar_equipoDisponible_guardaYNotifica() {
        // Arrange
        DisponibilidadEquipos disponibilidad = mock(DisponibilidadEquipos.class);
        PrestamoRepository repositorio = mock(PrestamoRepository.class);
        Notificador notificador = mock(Notificador.class);
        when(disponibilidad.estaDisponible(12)).thenReturn(true);
        when(repositorio.siguienteId()).thenReturn(50);
        SolicitarPrestamo caso = new SolicitarPrestamo(disponibilidad, repositorio, notificador);

        // Act
        Prestamo p = caso.ejecutar("1023", 12);

        // Assert
        assertEquals(50, p.getId());
        assertEquals("1023", p.getEstudianteId());
        assertEquals(12, p.getEquipoId());
        assertEquals(EstadoPrestamo.PENDIENTE, p.getEstado());
        verify(repositorio, times(1)).guardar(p);
        verify(notificador).notificar("1023", "Solicitud 50 registrada");
    }

    @Test
    void ejecutar_equipoNoDisponible_noGuardaNiNotifica() {
        // Arrange
        DisponibilidadEquipos disponibilidad = mock(DisponibilidadEquipos.class);
        PrestamoRepository repositorio = mock(PrestamoRepository.class);
        Notificador notificador = mock(Notificador.class);
        when(disponibilidad.estaDisponible(12)).thenReturn(false);
        SolicitarPrestamo caso = new SolicitarPrestamo(disponibilidad, repositorio, notificador);

        // Act + Assert
        EquipoNoDisponible ex = assertThrows(EquipoNoDisponible.class,
                () -> caso.ejecutar("1023", 12));
        assertEquals("Equipo 12 no disponible", ex.getMessage());
        verify(repositorio, never()).guardar(any());
        verifyNoInteractions(notificador);
    }

    @Test
    void ejecutar_falloAlGuardar_noNotifica() {
        // Arrange
        DisponibilidadEquipos disponibilidad = mock(DisponibilidadEquipos.class);
        PrestamoRepository repositorio = mock(PrestamoRepository.class);
        Notificador notificador = mock(Notificador.class);
        when(disponibilidad.estaDisponible(12)).thenReturn(true);
        when(repositorio.siguienteId()).thenReturn(50);
        doThrow(new RuntimeException("BD caída")).when(repositorio).guardar(any());
        SolicitarPrestamo caso = new SolicitarPrestamo(disponibilidad, repositorio, notificador);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> caso.ejecutar("1023", 12));
        assertEquals("BD caída", ex.getMessage());
        verify(notificador, never()).notificar(anyString(), anyString());
    }
}
