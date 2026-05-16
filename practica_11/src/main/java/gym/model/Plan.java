package gym.model;

import java.io.Serializable;

public enum Plan implements Serializable {
    BASICO("Básico", 25.0, 0.00, 10),
    ESTANDAR("Estándar", 45.0, 0.05, 20),
    PREMIUM("Premium", 75.0, 0.10, 50),
    VIP("VIP", 120.0, 0.20, 100);

    private final String nombre;
    private final double precio;
    private final double descuento;
    private final int puntosBonus;

    Plan(String nombre, double precio, double descuento, int puntosBonus) {
        this.nombre = nombre;
        this.precio = precio;
        this.descuento = descuento;
        this.puntosBonus = puntosBonus;
    }

    public String getNombre()          { return nombre; }
    public double getPrecio()          { return precio; }
    public double getDescuento()       { return descuento; }
    public int    getPuntosBonus()     { return puntosBonus; }
    public double getPrecioFinal()     { return precio * (1 - descuento); }

    @Override
    public String toString() { return nombre; }
}
