package domain;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class CalculadoraMultaTest {
    private static final LocalDate FECHA_LIMITE = LocalDate.of(2026, 11, 25);
    private final CalculadoraMulta calculadora = new CalculadoraMulta();

    @Test
    void devolucionUnDiaAntes_multaCero() {
        // Arrange
        LocalDate fechaDevolucion = LocalDate.of(2026, 11, 24);

        // Act
        int multa = calculadora.calcular(FECHA_LIMITE, fechaDevolucion, false);

        // Assert
        assertEquals(0, multa);
    }
}
