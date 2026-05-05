package practica_04;

public class CPUSensor extends sensor 
{
    private int numNucleos;

    public CPUSensor(String marca , String modelo , String num_serie , double umbral , int numNucleos)
    {
        super(marca , modelo , num_serie , umbral);
        this.numNucleos = numNucleos;
    }
    
    @Override
    public void actualizarLectura()
    {
        this.temperatura = 35.0 + (Math.random()* 40);
        System.out.println("CPU ["+ modelo + "]: Lectura promediada de " + numNucleos + "Nucleos");
    }


    @Override
    public boolean verificarEstado()
    {
        if (this.temperatura > this.umbral)
        {
            System.out.println("Eh wey se te anda quemando el CPU");
            return true;
        }
        return false;
    }

    @Override
    public void Alerta()
    {
        if(verificarEstado())
        {
            System.out.println("El componente se encuentra en riesgo de daño favor de verificar el estado del CPU" + modelo +"marca : " + marca);
        }
        else
        {
            System.out.println("Todo se encuentra bien");
        }
    }
}
