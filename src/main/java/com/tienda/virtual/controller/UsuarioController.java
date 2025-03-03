package com.tienda.virtual.controller;

import com.tienda.virtual.model.Usuario;
import com.tienda.virtual.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }


    @PostMapping("/iniciar-sesion")
    public ResponseEntity<Usuario> iniciarSesion(@RequestBody Usuario usuario) {
        Usuario usuarioAutenticado = usuarioService.iniciarSesion(usuario.getNombreUsuario(), usuario.getContraseña());

        if (usuarioAutenticado != null) {
            return ResponseEntity.ok(usuarioAutenticado); // respuesta 200 ok
        } else {
            // respuesta 401 no autorizado, build() construye la respuesta
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping
    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioService.obtenerTodosUsuarios();
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }

    @PutMapping
    public Usuario actualizarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.actualizarUsuario(usuario);
    }
}