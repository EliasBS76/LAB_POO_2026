
public class Eleccion_Propia 
{
    public static void main(String[] args)
    {
        int dinero_Ahorrado = 0;

        for (int n = 1 ; n <=365 ; n++)
        {
            dinero_Ahorrado = dinero_Ahorrado +10;
            System.out.println("día " + n +" Ahorro : " + dinero_Ahorrado);
        }
        System.out.println("Terminaste con : " + dinero_Ahorrado +"En 1 año");
    }
    
}
