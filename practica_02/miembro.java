package practica_02;

public class miembro 
{
    public String nombre;
    public int telefono;
    public int id;
    public String Tipo_Plan;
    public String Meses_inscrito;
    public boolean inscrito;

    //Informacion total
    public miembro (String nombre , int telefono,int id , String Tipo_Plan , String Meses_inscrito ,boolean inscrito)
    {
        this.nombre = nombre;
        this.telefono = telefono;
        this.id = id;
        this.Tipo_Plan = Tipo_Plan;
        this.Meses_inscrito = Meses_inscrito;
        this.inscrito = inscrito;
    }

    //Activos
    public miembro (String nombre , int id ,boolean inscrito)
    {
        this.nombre = nombre;
        this.id = id;
        this.inscrito = inscrito;
    }

    //Peticion de información
    public miembro (String nombre , int telefono)
    {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public void info()
    {
        System.out.println("ID DE USUARIO " + id + "Nombre : " + nombre + "Plan Actual : " + Tipo_Plan);
    }

    public void actualizar_Insicripcion()
    {
        this.inscrito = true;
        System.out.println("Se renovo la suscripcion con exito : " +nombre);

    }

    public void Dar_De_Baja()
    {
        this.inscrito = false;
        System.out.println("La suscripcion fue cancelada con exito : " + nombre);
    }

    public void actualizar_Info(String nombre , int telefono , String Tipo_Plan)
    {
        this.nombre = nombre;
        this.telefono = telefono;
        this.Tipo_Plan = Tipo_Plan;
        
    }

    public void actualizar(int telefono)
    {
        this.telefono = telefono;
    }

    public void actualizar(String Tipo_Plan)
    {
        this.Tipo_Plan = Tipo_Plan;
    }


    
}


