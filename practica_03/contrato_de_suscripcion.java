package practica_03;

public class contrato_de_suscripcion 
{
    protected String folio_contrato;
    protected String fecha_de_firmado;

    private miembro titular;

    public contrato_de_suscripcion(String folio_contrato , String fecha_de_firmado ,miembro titular )
    {
        this.folio_contrato = folio_contrato;
        this.fecha_de_firmado = fecha_de_firmado;
        this.titular = titular;

    }

    public void mostrar_contrato()
    {
        System.out.println("\n----FOLIO----------" + folio_contrato+"----");
        System.out.println("Fecha de inicio " + fecha_de_firmado);

        System.out.println("Datos del titular" + titular.toString());
    }
    protected void cancelar_contrato()
    {
        System.out.println("El siguiente contrato se dara por terminado debido a politicas internas del estrablecimiento : " + this.folio_contrato);
    }
    
}
