package com.tienda.virtual.service;

import com.tienda.virtual.model.Usuario;
import com.tienda.virtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // registro
    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña())); // Hashear la contraseña antes de guardar
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario iniciarSesion(String nombreUsuario, String contraseña) {
        Optional<Usuario> usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (usuario.isPresent() && passwordEncoder.matches(contraseña, usuario.get().getContraseña())) {
            return usuario.orElse(null);
        }
        return null;
    }

    @Override
    public List<Usuario> obtenerTodosUsuarios() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Nuevo registro
    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        // Si se envía una nueva contraseña y no está vacía o nula se hashea
        if (usuario.getContraseña() != null && !usuario.getContraseña().isEmpty()) {
            usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña())); // Hashear la contraseña antes de guardar
        }
        return usuarioRepository.save(usuario);
    }
}