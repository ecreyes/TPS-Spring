package com.tps.msusuario.servicio.impl;

import com.tps.msusuario.dominio.UsuarioRoot;
import com.tps.msusuario.repositorio.UsuarioJpaRepository;
import com.tps.msusuario.repositorio.entidad.Usuario;
import com.tps.msusuario.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioJpaRepository usuarioJpaRepository;

    public UsuarioServiceImpl(@Qualifier("usuarioJpaRespository") UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public void agregar(UsuarioRoot usuarioRoot) {
        Usuario usuario = new Usuario(usuarioRoot.getEmail(), usuarioRoot.getPassword(),
                usuarioRoot.getNombreUsuarioVO().getNombreUsuario(), usuarioRoot.getEstadoUsuarioVO().getEstado());
        usuarioJpaRepository.save(usuario);
    }

    @Override
    public int eliminar(UsuarioRoot usuarioRoot) {

        if (usuarioJpaRepository.findById(usuarioRoot.getId()).isPresent()) {

            usuarioJpaRepository.deleteById(usuarioRoot.getId());

            return 1;
        }
        return 0;
    }

    @Override
    public void editar(UsuarioRoot usuarioRoot) {

        if (usuarioJpaRepository.findById(usuarioRoot.getId()).isPresent()) {

            Usuario usuario = usuarioJpaRepository.findById(usuarioRoot.getId()).get();

            usuario.setEmail(usuarioRoot.getEmail());
            usuario.setPassword(usuarioRoot.getPassword());
            usuario.setNombreUsuario(usuarioRoot.getNombreUsuarioVO().getNombreUsuario());
            usuario.setEstado(usuarioRoot.getEstadoUsuarioVO().getEstado());

            usuarioJpaRepository.save(usuario);
        }
    }

    @Override
    public Map<String, Object> login(UsuarioRoot usuarioRoot) {
        Usuario usuario_bd = usuarioJpaRepository.findUsuarioByEmail(usuarioRoot.getEmail());

        Map<String, Object> result = new HashMap<>();

        if (usuario_bd != null) {

            //TODO: Mejorar seguridad
            if (usuarioRoot.getPassword().equals(usuario_bd.getPassword()) && usuarioRoot.getEmail().equals(usuario_bd.getEmail())) {

                //TODO: Quizas utilizar EstadoUsuarioVO para representar status de login
                result.put("Login_estado", "OK");

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", usuario_bd.getId());
                userMap.put("email", usuario_bd.getEmail());
                userMap.put("username", usuario_bd.getNombreUsuario());

                result.put("Usuario", userMap);
                return result;

            } else {
                result.put("Login_estado", "PASS INCORRECTA");
                return result;
            }
        } else {
            result.put("Login_estado", "USUARIO NO EXISTE");
            return result;
        }
    }
}
