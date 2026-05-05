package practica_03;

public class miembro 
{
    private String nombre;
    private long telefono;
    private int id;
    private String Tipo_Plan;
    private String Meses_inscrito;
    private boolean inscrito;

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

    //Creacion de los getters}

    public String getNombre(){return nombre;}
    public long getTelefono(){return telefono;}
    public int getID(){return id;}
    public String getTipoPlan(){return Tipo_Plan;}
    public String meses_inscrito(){return Meses_inscrito;}
    public boolean inscrito(){return inscrito;}

    //Validacion para que el telefono tenga 10 digitos
    public void Telefono_estructura(long verificacion)
    {
        String telString = String.valueOf(verificacion);
        if(telString.length() ==10)
        {
            this.telefono = verificacion;
            System.out.println("El telefono es valido");
        }
        else
        {
            System.out.println("El telefono no es correcto");
        }
    }

    public void plan(String plan_escogido)
    {
        if(plan_escogido != null && !plan_escogido.trim().isEmpty())
        {
            this.Tipo_Plan = plan_escogido;
            System.out.println("Plan actualizado a " + plan_escogido +"para" + this.nombre);
        }
        else
        {
            System.out.println("El plan no puedo ser nulo");
        }
    }


    
}


