package practica_06;

public class AutoElectrico extends Vehiculo implements IMantenible, IRastreable, IElectrico {

    private double capacidadBateriaKwh;
    private double porcentajeBateria;
    private double autonomiaMaxKm;
    private int kmUltimoMantenimiento;
    private double latitudActual;
    private double longitudActual;
    private static final int INTERVALO_MANTENIMIENTO = 20000;

    public AutoElectrico(String marca, String modelo, int anio, String placa,
                         double capacidadBateriaKwh, double autonomiaMaxKm) {
        super(marca, modelo, anio, placa);
        this.capacidadBateriaKwh    = capacidadBateriaKwh;
        this.autonomiaMaxKm         = autonomiaMaxKm;
        this.porcentajeBateria      = 100;
        this.kmUltimoMantenimiento  = 0;
        this.latitudActual          = 0;
        this.longitudActual         = 0;
    }

    @Override
    public double calcularValorComercial() {
        double base = 600000;
        double depreciacion = edadAnios() * 0.09;
        double degradacionBateria = (100 - porcentajeBateria) * 500;
        return base * Math.max(0.3, 1 - depreciacion) - degradacionBateria;
    }

    @Override
    public String tipoCombustible() {
        return "electrico";
    }

    @Override
    public double calcularConsumo() {
        return capacidadBateriaKwh / autonomiaMaxKm * 100;
    }

    @Override
    public boolean necesitaMantenimiento() {
        return (kilometraje - kmUltimoMantenimiento) >= INTERVALO_MANTENIMIENTO;
    }

    @Override
    public double calcularCostoMantenimiento() {
        double base = 800;
        if (edadAnios() > 3) base += 200;
        if (necesitaMantenimiento()) base += 250;
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

    @Override
    public double getPorcentajeBateria() {
        return porcentajeBateria;
    }

    @Override
    public double calcularAutonomiaRestante() {
        return autonomiaMaxKm * (porcentajeBateria / 100.0);
    }

    @Override
    public void cargarBateria(double porcentaje) {
        this.porcentajeBateria = Math.min(100, this.porcentajeBateria + porcentaje);
    }

    @Override
    public boolean necesitaCarga() {
        return porcentajeBateria <= 20;
    }

    public double getCapacidadBateriaKwh() { return capacidadBateriaKwh; }
    public double getAutonomiaMaxKm()      { return autonomiaMaxKm; }
}