package com.tps.msnoticias.dominio;
/**Categoria de la noticia como value object**/
public class CategoriaNoticiaVO {
  //atributos de la categoria
  private int id;
  private String nombre;

  //constructor de categoria por id
  CategoriaNoticiaVO(int id) {
    this.id = id;
  }

  //constructor de categoria
  CategoriaNoticiaVO(int id, String nombre) {
    this.id = id;
    this.nombre = nombre;
  }

  //getters y setters
  public int getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }
}
