package com.tps.msusuario.servicio;

import com.tps.msusuario.repositorio.entidad.Usuario;

import java.util.List;
import java.util.Map;

public interface UsuarioService {

	List<Usuario> getUsuarios();

	void agregarUsuario(Usuario usuario);

	int eliminarUsuario(Usuario usuario);

	Usuario editarUsuario(Usuario usuario);

	Map<String, Object> loginUsuario(Usuario usuario);

}
