package com.ts.apigateway.controlador;

import com.ts.apigateway.mensajeria.UsuarioMsg;
import com.ts.apigateway.modelo.Usuario;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de Usuario para la receción de peticiones
 **/
@RestController
public class UsuarioController {

  private final UsuarioMsg usuarioMsg;
  //Rutas utilizadas para enviar mensajes hacia colas receptoras
  private static final String ROUTE_KEY_CREATE = "usuario.crear";
  private static final String ROUTE_KEY_DELETE = "usuario.eliminar";
  private static final String ROUTE_KEY_EDIT = "usuario.editar";

  //BCrypt Hash
  private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(15);

  /**
   * Asignación del adaptador de usuario
   **/
  public UsuarioController(@Qualifier("usuarioMsgAdapter") UsuarioMsg usuarioMsg) {
    this.usuarioMsg = usuarioMsg;
  }

  /**
   * Agregar un usuario
   **/
  @PostMapping("/usuario/agregar")
  public Usuario agregar(@RequestBody Usuario usuario) {
    usuario.setPassword(encoder.encode(usuario.getPassword()));
    usuarioMsg.enviarMsg(usuario, ROUTE_KEY_CREATE);
    return usuario;
  }

  /**
   * Actualizar un usuario
   **/
  @PutMapping("/usuario/editar")
  public Usuario editar(@RequestBody Usuario usuario) {
    usuario.setPassword(encoder.encode(usuario.getPassword()));
    usuarioMsg.enviarMsg(usuario, ROUTE_KEY_EDIT);
    return usuario;
  }

  /**
   * Eliminar un usuario
   **/
  @DeleteMapping("/usuario/eliminar")
  public Usuario eliminar(@RequestBody Usuario usuario) {
    usuarioMsg.enviarMsg(usuario, ROUTE_KEY_DELETE);
    return usuario;
  }

  /**
   * Login de usuario
   **/
  @PostMapping("/usuario/login")
  public String login(@RequestBody Usuario usuario) {
    //TODO: Determinar si se debe hashear password en cada login (o solo hacer matches en msusuario,
    // implica enviar password por mensajeria en texto plano)
    return usuarioMsg.solicitarLogin(usuario);
  }
}
