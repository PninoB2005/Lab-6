
package data;

public class ServicioNoDisponible extends RuntimeException {

    public ServicioNoDisponible(String mensaje) {
        super(mensaje);
    }

    public ServicioNoDisponible(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
