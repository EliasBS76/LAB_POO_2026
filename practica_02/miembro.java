package practica_02;

public class miembro 
{
    public String nombre;
    public int telefono;
    public int id;
    public String Tipo_Plan;
    public String Meses_inscrito;
    public boolean inscrito;

    //Informacion completa del miembro
    public miembro (String nombre , int telefono,int id , String Tipo_Plan , String Meses_inscrito ,boolean inscrito)
    {
        this.nombre = nombre;
        this.telefono = telefono;
        this.id = id;
        this.Tipo_Plan = Tipo_Plan;
        this.Meses_inscrito = Meses_inscrito;
        this.inscrito = inscrito;
    }

    //Status del miembro
    public miembro (String nombre , int id ,boolean inscrito)
    {
        this.nombre = nombre;
        this.id = id;
        this.inscrito = inscrito;
    }

    //Peticion de informacion
    public miembro (String nombre , int telefono)
    {
        this.nombre = nombre;
        this.telefono = telefono;
    }
    
}


