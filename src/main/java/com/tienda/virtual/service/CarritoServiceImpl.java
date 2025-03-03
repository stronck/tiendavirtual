package com.tienda.virtual.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tienda.virtual.model.Producto;
import com.tienda.virtual.model.Usuario;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Locale;
import java.math.BigDecimal;
import java.util.List;


@Service
public class CarritoServiceImpl implements CarritoService {

    @Override
    public byte[] generarFactura(List<Producto> carrito, Usuario usuario) {
        if (carrito == null || carrito.isEmpty()) return null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, out);
            doc.open();

            // Datos de la empresa.
            doc.add(new Paragraph("TIENDA VIRTUAL: OriginalFragance", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25)));
            doc.add(new Paragraph("Nit 8815224657 - 8"));
            doc.add(new Paragraph("Tunja Centro Distrito 15"));
            doc.add(new Paragraph("www.corporativo@gmail.com"));

            // Título de la factura.
            doc.add(new Paragraph("Factura de Compra", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
            doc.add(new Paragraph(" ")); // Espacio

            // Datos básicos del comprador.
            doc.add(new Paragraph("Datos del Comprador:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            doc.add(new Paragraph("Usuario: " + usuario.getNombreUsuario()));
            doc.add(new Paragraph("Nombres: " + usuario.getNombres() + " " + usuario.getApellidos()));
            doc.add(new Paragraph("Dirección de entrega: " + usuario.getDireccionEnvio()));
            doc.add(new Paragraph(" "));

            // Título de productos comprados
            doc.add(new Paragraph("Productos Comprados:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            doc.add(new Paragraph(" ")); // Espacio

            // Tabla de productos.
            PdfPTable table = new PdfPTable(3);
            table.addCell(new PdfPCell(new Paragraph("Producto")));
            table.addCell(new PdfPCell(new Paragraph("Precio")));
            table.addCell(new PdfPCell(new Paragraph("Descripción")));


            // se importa NumberFormatLocale y se crea una instancia para ser llamada en donde se vaya mostrar el precio a miles
            NumberFormat formato = NumberFormat.getInstance(new Locale("es", "ES"));
            // variable auxiliar para iniciar en cero el total para operacion de suma
            BigDecimal total = BigDecimal.ZERO;
            for (Producto p : carrito) {
                table.addCell(p.getNombre());
                table.addCell("$" + formato.format(p.getPrecio()));   // precio formateado a miles
                table.addCell(p.getDescripcion());
                total = total.add(p.getPrecio()); // hace la operacion de suma(.add) inicializada en cero.ZERO
            }

            // Precio final formateado a miles e instrucciones de envío del comprador.
            doc.add(table);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Precio Total Facturado: $" + formato.format(total), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15)));
            doc.add(new Paragraph(" ")); // Espacio
            doc.add(new Paragraph("Los productos llegarán a la dirección registrada por el usuario. Los horarios de entrega son los sábados y domingos.", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            doc.add(new Paragraph(" ")); // Espacio
            doc.add(new Paragraph("Cualquier duda o inquietud comunicarse con nuestras líneas de atención. Horarios 24/7, líneas telefónicas WhatsApp 3112762773 - 3143221175.", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            doc.add(new Paragraph(" ")); // Espacio
            doc.add(new Paragraph("¡Muchas gracias por su compra!", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13)));

            doc.close(); // cierra el documento pdf y lo guarda en la variable out
            return out.toByteArray(); // retorna el archivo pdf en bytes
        } catch(Exception e) { // excepcion en caso de error en la generacion del pdf
            e.printStackTrace(); // imprime el error en consola
            return null; // retorna null en caso de error
        }
    }
}