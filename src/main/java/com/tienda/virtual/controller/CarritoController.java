package com.tienda.virtual.controller;

import com.tienda.virtual.model.Producto;
import com.tienda.virtual.model.Usuario;
import com.tienda.virtual.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// record es
record FacturaData(Usuario usuario, List<Producto> carrito) {}     // rremplazo de dto

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @PostMapping("/factura")
    public ResponseEntity<byte[]> generarFactura(@RequestBody FacturaData data) {
        byte[] pdf = carritoService.generarFactura(data.carrito(), data.usuario());
        if (pdf == null) return ResponseEntity.noContent().build(); // respuesta 204 no contenido .buid para construir la respuesta
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
