package practica_04;

public class sensor extends hardware_model {
    protected double temperatura;
    protected double umbral;

    
    public sensor(String marca, String modelo, String num_serie ,double umbral) {
        
        super(marca, modelo , num_serie); 
        
        this.umbral = umbral;
        this.temperatura = 0.0;
    }

    public void actualizarLectura() 
    {
        System.out.println("Realizando lectura básica del sensor...");
    }

    public boolean verificarEstado()
    {
        return temperatura > umbral;
    }

    public void Alerta()
    {
        System.out.println("Confirmando estado del equipo ");
    }
}
