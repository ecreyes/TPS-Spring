package com.tps.msusuario.servicio;

import com.tps.msusuario.dominio.UsuarioRoot;

import java.util.Map;
/**Interfaz para el servicio de usuario con los métodos
 *  de agregar,aleiminar,editar y login de usuario**/
public interface UsuarioService {

  void agregar(UsuarioRoot usuario);

  void eliminar(UsuarioRoot usuarioRoot);

  void editar(UsuarioRoot usuarioRoot);

  Map<String, Object> login(UsuarioRoot usuarioRoot);

}
