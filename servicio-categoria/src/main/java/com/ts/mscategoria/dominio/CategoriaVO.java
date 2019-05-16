package com.ts.mscategoria.dominio;

public class CategoriaVO {
	private String nombre;

	public CategoriaVO(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public String toString() {
		return "CategoriaVO{" +
				"nombre='" + nombre + '\'' +
				'}';
	}
}
