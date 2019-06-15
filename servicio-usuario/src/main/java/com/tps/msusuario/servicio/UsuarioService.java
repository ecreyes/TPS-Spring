package com.tps.msusuario.servicio;

import com.tps.msusuario.repositorio.entidad.Usuario;

import java.util.List;

public interface UsuarioService {

	List<Usuario> getUsuarios();

	void agregarUsuario(Usuario usuario);

	int eliminarUsuario(Usuario usuario);

	Usuario editarUsuario(Usuario usuario);

	String loginUsuario(Usuario usuario);

}
