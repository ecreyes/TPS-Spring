package com.tps.msusuario.servicio;

import com.tps.msusuario.dominio.UsuarioRoot;
import com.tps.msusuario.repositorio.entidad.Usuario;

import java.util.List;
import java.util.Map;

public interface UsuarioService {

	List<Usuario> getUsuarios();

	void agregarUsuario(UsuarioRoot usuario);

	int eliminarUsuario(UsuarioRoot usuarioRoot);

	void editarUsuario(UsuarioRoot usuarioRoot);

	Map<String, String> loginUsuario(UsuarioRoot usuarioRoot);

}
