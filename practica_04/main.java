package practica_04;

public class main {
    public static void main(String[] args) {
        
        GestorHardware gestor = new GestorHardware();

        // 2. Creamos los objetos de la jerarquía (Nivel 3)
        // Usamos datos parecidos a los de tu S22 para que sea real
        CPUSensor snapdragon = new CPUSensor("Samsung", "Snapdragon 8 Gen 1", "SN-888", 90.0, 8);
        BateriaSensor bateriaS22 = new BateriaSensor("Samsung", "Li-ion 4500mAh", "BAT-001", 45.0, 95);
        GPUSensor adreno = new GPUSensor("Qualcomm", "Adreno 730", "GPU-555", 85.0, 4);

        // 3. Los agregamos al contenedor (Polimorfismo en la entrada)
        gestor.agregarComponente(snapdragon);
        gestor.agregarComponente(bateriaS22);
        gestor.agregarComponente(adreno);

        // 4. Ejecutamos el monitoreo (Polimorfismo en la ejecución)
        gestor.monitorearSistema();
    }
}