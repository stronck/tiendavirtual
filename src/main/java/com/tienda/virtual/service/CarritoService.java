package com.tienda.virtual.service;

import com.tienda.virtual.model.Producto;
import com.tienda.virtual.model.Usuario;
import java.util.List;


public interface CarritoService {
    byte[] generarFactura(List<Producto> carrito, Usuario usuario);
}
