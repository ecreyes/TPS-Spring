package com.tps.msusuario.dominio;

public class UsuarioRoot {

	private String nombreUsuario;
	private String email;
	private String password;
	private EstadoUsuario estadoUsuario;

	public UsuarioRoot(String nombreUsuario, String email, String password,
	                   EstadoUsuario estadoUsuario) {
		this.nombreUsuario = nombreUsuario;
		this.email = email;
		this.password = password;
		this.estadoUsuario = estadoUsuario;
	}

	public UsuarioRoot() {
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public EstadoUsuario getEstadoUsuario() {
		return estadoUsuario;
	}

	public void setEstadoUsuario(EstadoUsuario estadoUsuario) {
		this.estadoUsuario = estadoUsuario;
	}
}
