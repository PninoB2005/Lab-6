package domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculadoraMulta {
    private static final int MULTA_POR_DIA = 2000;
    private static final int MULTA_MAXIMA = 50000;

    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long diasRetraso = ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);
        if (diasRetraso <= 0) {
            return 0;
        }
        int multa = (int) (diasRetraso * MULTA_POR_DIA);
        int multaConTope = Math.min(multa, MULTA_MAXIMA);
        if (esMonitor) {
            return multaConTope / 2;
        }
        return multaConTope;
    }
}
