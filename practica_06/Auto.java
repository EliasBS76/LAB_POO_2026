package practica_06;

public class Auto extends Vehiculo implements Mantenible, Rastreable {

    private int numeroPuertas;
    private int kmUltimoMantenimiento;
    private double latitudActual;
    private double longitudActual;
    private static final int INTERVALO_MANTENIMIENTO = 10000;

    public Auto(String marca, String modelo, int anio, String placa, int numeroPuertas) {
        super(marca, modelo, anio, placa);
        this.numeroPuertas          = numeroPuertas;
        this.kmUltimoMantenimiento  = 0;
        this.latitudActual          = 0;
        this.longitudActual         = 0;
    }

    @Override
    public double calcularValorComercial() {
        double base = 250000;
        double depreciacion = edadAnios() * 0.12;
        double porDesgaste  = (kilometraje / 100000.0) * 0.08;
        return base * Math.max(0.2, 1 - depreciacion - porDesgaste);
    }

    @Override
    public String tipoCombustible() {
        return "gasolina";
    }

    @Override
    public double calcularConsumo() {
        return 8.5 + (numeroPuertas * 0.3);
    }

    @Override
    public boolean necesitaMantenimiento() {
        return (kilometraje - kmUltimoMantenimiento) >= INTERVALO_MANTENIMIENTO;
    }

    @Override
    public double calcularCostoMantenimiento() {
        double base = 1200;
        if (edadAnios() > 5) base += 400;
        if (necesitaMantenimiento()) base += 300;
        return base;
    }

    @Override
    public void registrarMantenimiento(int kilometraje) {
        this.kmUltimoMantenimiento = kilometraje;
    }

    @Override
    public String estadoGeneral() {
        int kmDesde = kilometraje - kmUltimoMantenimiento;
        if (kmDesde >= INTERVALO_MANTENIMIENTO)        return "requiere mantenimiento urgente";
        if (kmDesde >= INTERVALO_MANTENIMIENTO * 0.8)  return "mantenimiento proximo";
        return "en buen estado";
    }

    @Override
    public void actualizarUbicacion(double latitud, double longitud) {
        this.latitudActual  = latitud;
        this.longitudActual = longitud;
    }

    @Override
    public String obtenerUbicacion() {
        return String.format("lat=%.4f, lon=%.4f", latitudActual, longitudActual);
    }

    @Override
    public double calcularDistanciaA(double latitud, double longitud) {
        double dLat = Math.toRadians(latitud - latitudActual);
        double dLon = Math.toRadians(longitud - longitudActual);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(latitudActual)) * Math.cos(Math.toRadians(latitud))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 6371 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    @Override
    public boolean estaEnZonaSegura(double latitudCentro, double longitudCentro, double radioKm) {
        return calcularDistanciaA(latitudCentro, longitudCentro) <= radioKm;
    }

    public int getNumeroPuertas() { return numeroPuertas; }
}