package com.ts.mscategoria.repositorio.entidad;

import javax.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria {

	@Id
	@GeneratedValue
	private int id;

	@Column(name = "nombre")
	private String nombre;

	public Categoria() {
	}

	public Categoria(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

    public int getId() {
        return id;
	}

	@Override
	public String toString() {
		return "Categoria{" +
				"id=" + id +
				", nombre='" + nombre + '\'' +
				'}';
	}
}