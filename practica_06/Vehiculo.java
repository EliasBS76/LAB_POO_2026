package practica_06;

public abstract class Vehiculo {

    protected String marca;
    protected String modelo;
    protected int anio;
    protected int kilometraje;
    protected String placa;

    public Vehiculo(String marca, String modelo, int anio, String placa) {
        this.marca       = marca;
        this.modelo      = modelo;
        this.anio        = anio;
        this.placa       = placa;
        this.kilometraje = 0;
    }

    public abstract double calcularValorComercial();
    public abstract String tipoCombustible();
    public abstract double calcularConsumo();

    public void agregarKilometros(int km) {
        if (km > 0) this.kilometraje += km;
    }

    public int edadAnios() {
        return 2025 - anio;
    }

    public String resumen() {
        return placa + " | " + marca + " " + modelo + " " + anio
             + " | " + kilometraje + " km | " + tipoCombustible()
             + " | valor: $" + String.format("%.2f", calcularValorComercial());
    }

    public String getMarca()     { return marca; }
    public String getModelo()    { return modelo; }
    public int getAnio()         { return anio; }
    public int getKilometraje()  { return kilometraje; }
    public String getPlaca()     { return placa; }

    @Override
    public String toString() {
        return placa + " " + marca + " " + modelo;
    }
}