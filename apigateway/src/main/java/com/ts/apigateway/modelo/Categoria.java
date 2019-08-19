package com.ts.apigateway.modelo;

import java.io.Serializable;
/*
Clase categoria correspondiente a la logica
de negocio.
 */
public class Categoria implements Serializable {
  //atributos de la clase categoria
  private int id;
  private String nombre;
  private String estado;

  //constructor para crear la clase categoria.
  public Categoria(int id, String nombre, String estado) {
    this.id = id;
    this.nombre = nombre;
    this.estado = estado;
  }

  //getters y setters de la clase categoria
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  //método que entrega la categoria como string con sus propieades
  @Override
  public String toString() {
    return "Categoria{" +
        "id=" + id +
        ", nombre='" + nombre + '\'' +
        ", estado='" + estado + '\'' +
        '}';
  }
}
