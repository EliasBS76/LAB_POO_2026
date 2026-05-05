package practica_02;
import java.util.ArrayList;
public class gimnasio 
{
    private ArrayList<miembro> listaMiembros;

public gimnasio()
{
    this.listaMiembros = new ArrayList();
}

//Agregar miembros al gym

public void agregarMiembro(miembro nuevoMiembro)
{
    listaMiembros.add(nuevoMiembro);
    System.out.println("Se pudo agregar correctamente a : " + nuevoMiembro.nombre);

}

public void mostrar_Miembros()
{
    System.out.println("LISTA DE MIEMBROS ACTUALES");
    if(listaMiembros.isEmpty())
    {
        System.out.println("No se encuentra ningun miembro registrado , si este dato es erroneo favot de contactar al equipo de IT");

    }
    else
    {
        for(miembro m : listaMiembros)
        {
            System.out.println("ID " +m.id + "Nombre : " + m.nombre + "PLAN : " + m.Tipo_Plan);
        }
    }
}

public miembro buscar_ID(int id_deseado)
{
    for (miembro m : listaMiembros)
    {
        if(m.id == id_deseado)
        {
            return m;
        }
    }
    return null;
}

}