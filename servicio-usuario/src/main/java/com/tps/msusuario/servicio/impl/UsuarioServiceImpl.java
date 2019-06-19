package com.tps.msusuario.servicio.impl;

import com.tps.msusuario.repositorio.UsuarioJpaRepository;
import com.tps.msusuario.repositorio.entidad.Usuario;
import com.tps.msusuario.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioJpaRepository usuarioJpaRepository;

    public UsuarioServiceImpl(@Qualifier("usuarioJpaRespository") UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public List<Usuario> getUsuarios() {
        return usuarioJpaRepository.findAll();
    }

    @Override
    public void agregarUsuario(Usuario usuario) {
        usuarioJpaRepository.save(usuario);
    }

    @Override
    public int eliminarUsuario(Usuario usuario) {
        usuarioJpaRepository.delete(usuario);
        return 0;
    }

    @Override
    public Usuario editarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public Map<String, Object> loginUsuario(Usuario usuario) {
        Usuario usuario_bd = usuarioJpaRepository.findUsuarioByEmail(usuario.getEmail());

        Map<String, Object> result = new HashMap<>();

        if (usuario_bd != null) {

            //TODO: Mejorar seguridad
            if (usuario.getPassword().equals(usuario_bd.getPassword())) {

                //TODO: Quizas utilizar EstadoUsuario para representar status de login
                result.put("STATUS", "OK");
                result.put("Usuario", usuario_bd);
                return result;

            } else {
                result.put("STATUS", "PASS INCORRECTA");
                return result;
            }
        } else {
            result.put("STATUS", "USUARIO NO EXISTE");
            return result;
        }
    }
}
