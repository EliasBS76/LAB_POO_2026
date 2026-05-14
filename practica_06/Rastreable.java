package practica_06;

public interface Rastreable {
    void actualizarUbicacion(double latitud, double longitud);
    String obtenerUbicacion();
    double calcularDistanciaA(double latitud, double longitud);
    boolean estaEnZonaSegura(double latitudCentro, double longitudCentro, double radioKm);
}