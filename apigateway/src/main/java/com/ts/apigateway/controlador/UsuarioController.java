package com.ts.apigateway.controlador;

import com.ts.apigateway.mensajeria.UsuarioMsg;
import com.ts.apigateway.modelo.Usuario;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**Controlador de Usuario para la receción de peticiones**/
@RestController
public class UsuarioController {

  private final UsuarioMsg usuarioMsg;
  /**Nombre de las colas que estan en Rabbit para el envio de mensajes**/
  private static final String ROUTE_KEY_CREATE = "usuario.crear";
  private static final String ROUTE_KEY_DELETE = "usuario.eliminar";
  private static final String ROUTE_KEY_EDIT = "usuario.editar";

  /**Asignación del adaptador de usuario**/
  public UsuarioController(@Qualifier("usuarioMsgAdapter") UsuarioMsg usuarioMsg) {
    this.usuarioMsg = usuarioMsg;
  }

  /**Agregar un usuario**/
  @PostMapping("/usuario/agregar")
  public Usuario agregar(@RequestBody Usuario usuario) {
    usuarioMsg.enviarMsg(usuario, ROUTE_KEY_CREATE);
    return usuario;
  }

  /**Actualizar un usuario**/
  @PutMapping("/usuario/editar")
  public Usuario editar(@RequestBody Usuario usuario) {
    usuarioMsg.enviarMsg(usuario, ROUTE_KEY_EDIT);
    return usuario;
  }

  /**Eliminar un usuario**/
  @DeleteMapping("/usuario/eliminar")
  public Usuario eliminar(@RequestBody Usuario usuario) {
    usuarioMsg.enviarMsg(usuario, ROUTE_KEY_DELETE);
    return usuario;
  }

  /**Login de usuario**/
  @PostMapping("/usuario/login")
  public String login(@RequestBody Usuario usuario) {
    return usuarioMsg.solicitarLogin(usuario);
  }
}
