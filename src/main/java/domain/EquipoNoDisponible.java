package domain;

public class EquipoNoDisponible extends RuntimeException {
    public EquipoNoDisponible(int equipoId) {
        super("Equipo " + equipoId + " no disponible");
    }
}
