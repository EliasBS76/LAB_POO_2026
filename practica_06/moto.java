package practica_06;

public class Moto extends Vehiculo implements Mantenible {

    private int cilindrada;
    private boolean esSport;
    private int kmUltimoMantenimiento;
    private static final int INTERVALO_MANTENIMIENTO = 6000;

    public Moto(String marca, String modelo, int anio, String placa,
        int cilindrada, boolean esSport) {
        super(marca, modelo, anio, placa);
        this.cilindrada             = cilindrada;
        this.esSport                = esSport;
        this.kmUltimoMantenimiento  = 0;
    }

    @Override
    public double calcularValorComercial() {
        double base = 80000;
        if (esSport) base += 40000;
        double depreciacion = edadAnios() * 0.10;
        return base * Math.max(0.15, 1 - depreciacion);
    }

    @Override
    public String tipoCombustible() {
        return esSport ? "gasolina premium" : "gasolina";
    }

    @Override
    public double calcularConsumo() {
        double base = cilindrada / 100.0 * 0.9;
        return esSport ? base * 1.3 : base;
    }

    @Override
    public boolean necesitaMantenimiento() {
        return (kilometraje - kmUltimoMantenimiento) >= INTERVALO_MANTENIMIENTO;
    }

    @Override
    public double calcularCostoMantenimiento() {
        double base = 600;
        if (esSport) base += 250;
        if (necesitaMantenimiento()) base += 150;
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

    public int getCilindrada() { return cilindrada; }
    public boolean isEsSport() { return esSport; }
}
