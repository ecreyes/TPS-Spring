package com.tps.msusuario.servicio;

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

  public UsuarioServiceImpl(
      @Qualifier("usuarioJpaRespository") UsuarioJpaRepository usuarioJpaRepository) {
    this.usuarioJpaRepository = usuarioJpaRepository;
  }

  @Override
  public void agregar(UsuarioRoot usuarioRoot) {
    Usuario usuario = new Usuario(usuarioRoot.getEmail(), usuarioRoot.getPassword(),
        usuarioRoot.getNombreUsuarioVO().getNombreUsuario(),
        usuarioRoot.getEstadoUsuarioVO().getEstado());
    usuarioJpaRepository.save(usuario);
  }

  @Override
  public void eliminar(UsuarioRoot usuarioRoot) {

    if (usuarioJpaRepository.findById(usuarioRoot.getId()).isPresent()) {

      usuarioJpaRepository.deleteById(usuarioRoot.getId());
    }
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

    Usuario usuarioBd = usuarioJpaRepository.findUsuarioByEmail(usuarioRoot.getEmail());

    Map<String, Object> result = new HashMap<>();

    if (usuarioBd != null) {

      //TODO: Mejorar seguridad
      if (usuarioRoot.getPassword().equals(usuarioBd.getPassword()) && usuarioRoot.getEmail()
          .equals(usuarioBd.getEmail())) {

        //TODO: Quizas utilizar EstadoUsuarioVO para representar status de login
        result.put("Login_estado", "OK");

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", usuarioBd.getId());
        userMap.put("email", usuarioBd.getEmail());
        userMap.put("username", usuarioBd.getNombreUsuario());

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
