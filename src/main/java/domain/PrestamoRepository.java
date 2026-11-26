package domain;

public interface PrestamoRepository {
    int siguienteId();
    void guardar(Prestamo p);
}
