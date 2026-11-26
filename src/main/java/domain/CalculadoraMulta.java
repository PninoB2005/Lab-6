package domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculadoraMulta {
    private static final int MULTA_POR_DIA = 2000;

    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long diasRetraso = ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);
        if (diasRetraso <= 0) {
            return 0;
        }
        return (int) (diasRetraso * MULTA_POR_DIA);
    }
}
