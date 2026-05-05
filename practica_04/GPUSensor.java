package practica_04;

public class GPUSensor extends sensor 
{
    private int numNucleeos;
    public GPUSensor(String marca , String modelo , String num_serie , double umbral , int numNucleeos)

    {
        super(marca , modelo , num_serie , umbral);
        this.numNucleeos = numNucleeos;
    }
    
    @Override
    public void actualizarLectura()
    {
        this.temperatura = 50.0 + (Math.random() * 40);
        System.out.println("GPU ["+ modelo + "]: Lectura promediada de " + numNucleeos + "Celdas");
    }

    @Override
    public boolean verificarEstado()
    {
        if (this.temperatura > this.umbral)
        {
            System.out.println("Eh wey se te anda quemando la GPU");
            return true;
        }
        return false;
    }

    @Override
    public void Alerta()
    {
        if(verificarEstado())
        {
            System.out.println("El componente se encuentra en riesgo de daño favor de verificar el estado de la GPU" + modelo +"marca : " + marca);
        }
        else
        {
            System.out.println("Todo se encuentra bien");
        }
    }
    
}
