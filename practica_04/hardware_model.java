package practica_04;

public class hardware_model 
{
    protected String marca;
    protected String modelo;
    protected String num_serie;

    public  hardware_model(String marca , String modelo , String num_serie)
    {
        this.marca = marca;
        this. modelo = modelo;
        this.num_serie = num_serie;   
    }

    public void info()
    {
        System.out.println("Dispositivo : " + marca +" " + modelo +"Con serie" + num_serie);
    }
    


    
}
