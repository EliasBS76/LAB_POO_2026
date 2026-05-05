package practica_02;

public class ejercicio_02 
{
    public static void main(String[] args)
    {
        gimnasio power_of_heroes = new gimnasio();

    
        
        miembro m1 = new miembro("Elías", 811234567, 2069356, "VIP", "12 meses", true);
        
        miembro m2 = new miembro("Isabella", 2010001, true);
        miembro m3 = new miembro("Elva", 2010002, false);
        
        
        miembro m4 = new miembro("Emiliano", 2010003); 
        miembro m5 = new miembro("Carlos", 2010004);

        
        System.out.println("--- Agregando miembros ---");
        power_of_heroes.agregarMiembro(m1);
        power_of_heroes.agregarMiembro(m2);
        power_of_heroes.agregarMiembro(m3);
        power_of_heroes.agregarMiembro(m4);
        power_of_heroes.agregarMiembro(m5);

        power_of_heroes.mostrar_Miembros();

        
        System.out.println("\n--- Buscando miembro por ID ---");
        int idABuscar = 2069356;
        miembro encontrado = power_of_heroes.buscar_ID(idABuscar);

        if (encontrado != null) {
            System.out.println("¡Miembro encontrado! Nombre: " + encontrado.nombre + ", Plan: " + encontrado.Tipo_Plan);
        } else {
            System.out.println("No se encontró ningún miembro con el ID: " + idABuscar);
        }
    }
    }

