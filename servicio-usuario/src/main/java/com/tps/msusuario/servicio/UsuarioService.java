package com.tps.msusuario.servicio;

import com.tps.msusuario.repositorio.entidad.Usuario;

import java.util.List;

public interface UsuarioService {

	public abstract List<Usuario> getUsuarios();

	public abstract Usuario agregarUsuario(Usuario usuario);

	public abstract int eliminarUsuario(Usuario usuario);

	public abstract Usuario editarUsuario(Usuario usuario);

}
