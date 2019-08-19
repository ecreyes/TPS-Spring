package com.ts.mscategoria.repositorio.entidad;

import javax.persistence.*;

/**Entidad de categoria usada por el JPA**/
@Entity
@Table(name = "categoria")
public class Categoria {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private int id;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "estado")
  private String estado;

  public Categoria() {
  }

  public Categoria(String nombre, String estado) {
    this.nombre = nombre;
    this.estado = estado;
  }

  public int getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  @Override
  public String toString() {
    return "Categoria{" +
        "id=" + id +
        ", nombre='" + nombre + '\'' +
        ", estado='" + estado + '\'' +
        '}';
  }
}