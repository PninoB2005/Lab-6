package domain;

public class SolicitarPrestamo {

    private final DisponibilidadEquipos disponibilidad;
    private final PrestamoRepository repositorio;
    private final Notificador notificador;

    public SolicitarPrestamo(DisponibilidadEquipos disponibilidad,
                             PrestamoRepository repositorio,
                             Notificador notificador) {
        this.disponibilidad = disponibilidad;
        this.repositorio = repositorio;
        this.notificador = notificador;
    }

    public Prestamo ejecutar(String estudianteId, int equipoId) {
        if (!disponibilidad.estaDisponible(equipoId)) {
            throw new EquipoNoDisponible(equipoId);
        }
        int id = repositorio.siguienteId();
        Prestamo p = new Prestamo(id, estudianteId, equipoId);
        repositorio.guardar(p);
        notificador.notificar(estudianteId, "Solicitud " + p.getId() + " registrada");
        return p;
    }
}
