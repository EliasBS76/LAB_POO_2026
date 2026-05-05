package practica_03;

public class main {
    public static void main(String[] args) 
    {
        System.out.println("=== INICIANDO PRUEBAS DE ENCAPSULAMIENTO ===\n");

        // 1. Crear el objeto
        miembro socio1 = new miembro("Elías Bernal", 2069356);

        // 2. PRUEBA DE VALIDACIÓN 1: Teléfono
        System.out.println(">> Probando validación de Teléfono:");
        socio1.Telefono_estructura(812345); // Intento fallido (muy corto)
        socio1.Telefono_estructura(8112345678L); // Intento exitoso (10 dígitos, la 'L' indica que es tipo long)

        // 3. PRUEBA DE VALIDACIÓN 2: Plan
        System.out.println("\n>> Probando validación de Plan:");
        socio1.plan("Estudiante"); // Intento fallido (no existe ese plan)
        socio1.plan("VIP"); // Intento exitoso

        // 4. PRUEBA DE toString()
        System.out.println("\n>> Imprimiendo objeto con toString():");
        System.out.println(socio1.toString());

        // 5. PRUEBA DE COMPOSICIÓN Y PROTECTED
        System.out.println("\n>> Probando Composición y Protected:");
        contrato_de_suscripcion contratoElias = new contrato_de_suscripcion("FOL-001", "05/05/2026", socio1);
        contratoElias.mostrar_contrato();
        
        
        System.out.println("Accediendo al atributo protected directamente: " + contratoElias.folio_contrato);
    }
}