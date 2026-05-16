package gym.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import gym.model.*;
import javafx.concurrent.Task;

import java.awt.Color;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class ReporteService extends Task<String> {

    private final DataService ds = DataService.getInstance();

    @Override
    protected String call() throws Exception {
        updateMessage("Recopilando estadísticas…");
        updateProgress(0, 6);

        String filename = "reporte_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

        Document doc = new Document(PageSize.A4, 50, 50, 60, 50);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filename));
        doc.open();

        Thread.sleep(200);
        updateProgress(1, 6);
        updateMessage("Generando encabezado…");
        agregarEncabezado(doc, writer);

        Thread.sleep(200);
        updateProgress(2, 6);
        updateMessage("Sección de clientes…");
        agregarSeccionClientes(doc);

        Thread.sleep(200);
        updateProgress(3, 6);
        updateMessage("Sección de ingresos…");
        agregarSeccionIngresos(doc);

        Thread.sleep(200);
        updateProgress(4, 6);
        updateMessage("Sección de pagos…");
        agregarSeccionPagos(doc);

        Thread.sleep(200);
        updateProgress(5, 6);
        updateMessage("Sección de inventario…");
        agregarSeccionEquipos(doc);

        doc.close();
        updateProgress(6, 6);
        updateMessage("PDF generado: " + filename);
        return filename;
    }

    

    private void agregarEncabezado(Document doc, PdfWriter writer) throws DocumentException {
        // Fondo de cabecera
        PdfContentByte cb = writer.getDirectContent();
        cb.setColorFill(new Color(21, 101, 192));
        cb.rectangle(50, doc.getPageSize().getHeight() - 110, doc.getPageSize().getWidth() - 100, 70);
        cb.fill();

        Font fTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.WHITE);
        Font fSub   = FontFactory.getFont(FontFactory.HELVETICA, 11, new Color(180, 210, 255));

        Paragraph title = new Paragraph("GymManager Pro — Reporte de Estadísticas", fTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(10);

        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                new java.util.Locale("es", "ES")));
        Paragraph subtitle = new Paragraph("Generado el " + fecha, fSub);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);

        doc.add(title);
        doc.add(subtitle);
        doc.add(new LineSeparator(1, 100, new Color(21, 101, 192), Element.ALIGN_CENTER, -5));
        doc.add(Chunk.NEWLINE);
    }

    private void agregarSeccionClientes(Document doc) throws DocumentException {
        List<Cliente> clientes = ds.getClientes();
        doc.add(tituloSeccion("Estadísticas de Clientes"));

        long activos  = clientes.stream().filter(c -> c.getMembresiaActiva() != null).count();
        long vencidos = clientes.size() - activos;

        PdfPTable stats = tablaDos(60);
        agregarFila(stats, "Total de clientes registrados", String.valueOf(clientes.size()));
        agregarFila(stats, "Con membresía activa",          String.valueOf(activos));
        agregarFila(stats, "Sin membresía activa",          String.valueOf(vencidos));
        doc.add(stats);
        doc.add(Chunk.NEWLINE);

        
        doc.add(parrafoSub("Distribución por plan:"));
        PdfPTable planTable = tablaCabecera(new String[]{"Plan", "Clientes", "Precio mensual"}, 80);
        for (Plan plan : Plan.values()) {
            long cnt = clientes.stream()
                    .filter(c -> c.getMembresiaActiva() != null &&
                                 c.getMembresiaActiva().getPlan() == plan).count();
            planTable.addCell(celda(plan.getNombre(), false));
            planTable.addCell(celda(String.valueOf(cnt), false));
            planTable.addCell(celda(String.format("$%.2f", plan.getPrecioFinal()), false));
        }
        doc.add(planTable);
        doc.add(Chunk.NEWLINE);
    }

    private void agregarSeccionIngresos(Document doc) throws DocumentException {
        doc.add(tituloSeccion("Ingresos Proyectados (mensual)"));

        double total = 0;
        PdfPTable t = tablaCabecera(new String[]{"Plan", "Clientes activos", "Ingreso"}, 80);
        for (Plan plan : Plan.values()) {
            long cnt = ds.getClientes().stream()
                    .filter(c -> c.getMembresiaActiva() != null &&
                                 c.getMembresiaActiva().getPlan() == plan).count();
            double ing = cnt * plan.getPrecioFinal();
            total += ing;
            t.addCell(celda(plan.getNombre(),          false));
            t.addCell(celda(String.valueOf(cnt),       false));
            t.addCell(celda(String.format("$%.2f", ing), false));
        }
        
        PdfPCell cTotal = new PdfPCell(new Phrase("TOTAL PROYECTADO",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        cTotal.setColspan(2);
        cTotal.setBackgroundColor(new Color(220, 232, 252));
        cTotal.setPadding(6);
        PdfPCell vTotal = new PdfPCell(new Phrase(String.format("$%.2f", total),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        vTotal.setBackgroundColor(new Color(220, 232, 252));
        vTotal.setPadding(6);
        t.addCell(cTotal);
        t.addCell(vTotal);
        doc.add(t);
        doc.add(Chunk.NEWLINE);
    }

    private void agregarSeccionPagos(Document doc) throws DocumentException {
        List<Pago> pagos = ds.getPagos();
        doc.add(tituloSeccion("Historial de Pagos"));

        long completados = pagos.stream().filter(p -> p.getEstado() == EstadoPago.COMPLETADO).count();
        long rechazados  = pagos.stream().filter(p -> p.getEstado() == EstadoPago.RECHAZADO).count();
        double totalCobrado = pagos.stream()
                .filter(p -> p.getEstado() == EstadoPago.COMPLETADO)
                .mapToDouble(Pago::getMonto).sum();

        PdfPTable resumen = tablaDos(65);
        agregarFila(resumen, "Total de transacciones",  String.valueOf(pagos.size()));
        agregarFila(resumen, "Pagos completados",       String.valueOf(completados));
        agregarFila(resumen, "Pagos rechazados",        String.valueOf(rechazados));
        agregarFila(resumen, "Total recaudado",         String.format("$%.2f", totalCobrado));
        doc.add(resumen);
        doc.add(Chunk.NEWLINE);

        if (!pagos.isEmpty()) {
            doc.add(parrafoSub("Últimas transacciones:"));
            PdfPTable tabla = tablaCabecera(
                    new String[]{"Cliente", "Plan", "Monto", "Método", "Estado"}, 100);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM HH:mm");
            for (Pago p : pagos.stream().sorted(
                    (a, b) -> b.getFecha().compareTo(a.getFecha())).limit(10).toList()) {
                tabla.addCell(celda(p.getCliente().getNombre(), false));
                tabla.addCell(celda(p.getPlan().getNombre(),    false));
                tabla.addCell(celda(String.format("$%.2f", p.getMonto()), false));
                tabla.addCell(celda(p.getMetodo(),              false));
                PdfPCell cEst = celda(p.getEstado().name(),    false);
                cEst.setBackgroundColor(p.getEstado() == EstadoPago.COMPLETADO
                        ? new Color(200, 240, 200) : new Color(255, 200, 200));
                tabla.addCell(cEst);
            }
            doc.add(tabla);
        }
        doc.add(Chunk.NEWLINE);
    }

    private void agregarSeccionEquipos(Document doc) throws DocumentException {
        doc.add(tituloSeccion("Inventario de Equipos"));
        PdfPTable t = tablaCabecera(new String[]{"Equipo", "Tipo", "Cantidad", "Estado"}, 100);
        for (Equipo e : ds.getEquipos()) {
            t.addCell(celda(e.getNombre(), false));
            t.addCell(celda(e.getTipo(),   false));
            t.addCell(celda(String.valueOf(e.getCantidad()), false));
            PdfPCell est = celda(e.getEstado().name(), false);
            est.setBackgroundColor(switch (e.getEstado()) {
                case DISPONIBLE        -> new Color(200, 240, 200);
                case EN_MANTENIMIENTO  -> new Color(255, 235, 180);
                case FUERA_DE_SERVICIO -> new Color(255, 200, 200);
            });
            t.addCell(est);
        }
        doc.add(t);
    }

    

    private Paragraph tituloSeccion(String texto) {
        Font f = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, new Color(21, 101, 192));
        Paragraph p = new Paragraph(texto, f);
        p.setSpacingBefore(10); p.setSpacingAfter(6);
        return p;
    }

    private Paragraph parrafoSub(String texto) {
        Font f = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.DARK_GRAY);
        Paragraph p = new Paragraph(texto, f);
        p.setSpacingAfter(4);
        return p;
    }

    private PdfPTable tablaDos(float widthPct) throws DocumentException {
        PdfPTable t = new PdfPTable(2);
        t.setWidthPercentage(widthPct);
        t.setWidths(new float[]{3, 1.5f});
        return t;
    }

    private PdfPTable tablaCabecera(String[] headers, float widthPct) throws DocumentException {
        PdfPTable t = new PdfPTable(headers.length);
        t.setWidthPercentage(widthPct);
        Font hf = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        for (String h : headers) {
            PdfPCell c = new PdfPCell(new Phrase(h, hf));
            c.setBackgroundColor(new Color(21, 101, 192));
            c.setPadding(6);
            t.addCell(c);
        }
        return t;
    }

    private void agregarFila(PdfPTable t, String label, String value) {
        Font lf = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font vf = FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell lc = new PdfPCell(new Phrase(label, lf));
        lc.setBackgroundColor(new Color(240, 245, 255)); lc.setPadding(5);
        PdfPCell vc = new PdfPCell(new Phrase(value, vf)); vc.setPadding(5);
        t.addCell(lc); t.addCell(vc);
    }

    private PdfPCell celda(String texto, boolean bold) {
        Font f = bold ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)
                      : FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell c = new PdfPCell(new Phrase(texto, f));
        c.setPadding(5);
        return c;
    }
}
