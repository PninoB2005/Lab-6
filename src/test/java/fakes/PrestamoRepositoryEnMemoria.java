package fakes;

import domain.Prestamo;
import domain.PrestamoRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrestamoRepositoryEnMemoria implements PrestamoRepository {

    private final List<Prestamo> prestamos = new ArrayList<>();
    private int contador = 1;

    @Override
    public int siguienteId() {
        return contador++;
    }

    @Override
    public void guardar(Prestamo prestamo) {
        prestamos.add(prestamo);
    }

    public List<Prestamo> obtenerTodos() {
        return Collections.unmodifiableList(prestamos);
    }

    public int cantidadPrestamos() {
        return prestamos.size();
    }
}
