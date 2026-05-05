package practica_04;

public class BateriaSensor extends sensor
{
    private int numCeldas;
    public BateriaSensor(String marca , String modelo , String num_serie , double umbral , int numCeldas)

    {
        super(marca , modelo , num_serie , umbral);
        this.numCeldas = numCeldas;
    }
    
    @Override
    public void actualizarLectura()
    {
        this.temperatura = 45.0 + (Math.random() * 40);
        System.out.println("Bateria ["+ modelo + "]: Lectura promediada de " + numCeldas + "Celdas");
    }

    @Override
    public boolean verificarEstado()
    {
        if (this.temperatura > this.umbral)
        {
            System.out.println("Eh wey se te anda quemando la pila");
            return true;
        }
        return false;
    }

    @Override
    public void Alerta()
    {
        if(verificarEstado())
        {
            System.out.println("El componente se encuentra en riesgo de daño favor de verificar el estado de la bateria" + modelo +"marca : " + marca);
        }
        else
        {
            System.out.println("Todo se encuentra bien");
        }
    }

}
