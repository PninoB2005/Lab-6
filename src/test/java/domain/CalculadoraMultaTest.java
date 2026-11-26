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

    @Test
    void devolucionUnDiaTarde_multaDosMil() {
        // Arrange
        LocalDate fechaDevolucion = LocalDate.of(2026, 11, 26);

        // Act
        int multa = calculadora.calcular(FECHA_LIMITE, fechaDevolucion, false);

        // Assert
        assertEquals(2000, multa);
    }

    @Test
    void devolucionCuarentaDiasTarde_aplicaTopeCincuentaMil() {
        // Arrange
        LocalDate fechaDevolucion = LocalDate.of(2027, 1, 4);

        // Act
        int multa = calculadora.calcular(FECHA_LIMITE, fechaDevolucion, false);

        // Assert
        assertEquals(50000, multa);
    }

    @Test
    void monitorTresDiasTarde_aplicaMitad() {
        // Arrange
        LocalDate fechaDevolucion = LocalDate.of(2026, 11, 28);

        // Act
        int multa = calculadora.calcular(FECHA_LIMITE, fechaDevolucion, true);

        // Assert
        assertEquals(3000, multa);
    }

    @Test
    void devolucionEnElLimite_multaCero() {
        // Arrange
        LocalDate fechaDevolucion = LocalDate.of(2026, 11, 25);

        // Act
        int multa = calculadora.calcular(FECHA_LIMITE, fechaDevolucion, false);

        // Assert
        assertEquals(0, multa);
    }

    @Test
    void devolucionVeinticincoDiasTarde_aplicaTopeCincuentaMil() {
        // Arrange
        LocalDate fechaDevolucion = LocalDate.of(2026, 12, 20);

        // Act
        int multa = calculadora.calcular(FECHA_LIMITE, fechaDevolucion, false);

        // Assert
        assertEquals(50000, multa);
    }

    @Test
    void monitorCuarentaDiasTarde_mitadDelTope() {
        // Arrange
        LocalDate fechaDevolucion = LocalDate.of(2027, 1, 4);

        // Act
        int multa = calculadora.calcular(FECHA_LIMITE, fechaDevolucion, true);

        // Assert
        assertEquals(25000, multa);
    }
}
