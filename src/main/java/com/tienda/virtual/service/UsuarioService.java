package com.tienda.virtual.service;

import com.tienda.virtual.model.Usuario;

import java.util.List;

public interface UsuarioService {
    Usuario registrarUsuario(Usuario usuario);

    Usuario iniciarSesion(String nombreUsuario, String contraseña);

    List<Usuario> obtenerTodosUsuarios();

    void eliminarUsuario(Long id);

    Usuario actualizarUsuario(Usuario usuario);
}