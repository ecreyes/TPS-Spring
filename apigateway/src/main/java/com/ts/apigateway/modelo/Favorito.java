package com.ts.apigateway.modelo;
/*
Clase de favorito para representar la logica
de negocio.
 */
public class Favorito {
  //atributos de la clase de favorito
  private int id;
  private int id_usuario;
  private int id_noticia;
  private String fechaFavorito;

  //constructor para crear un favorito
  public Favorito(int id, int id_usuario, int id_noticia, String fechaFavorito) {
    this.id = id;
    this.id_usuario = id_usuario;
    this.id_noticia = id_noticia;
    this.fechaFavorito = fechaFavorito;
  }

  //getters y setters de favorito
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId_usuario() {
    return id_usuario;
  }

  public void setId_usuario(int id_usuario) {
    this.id_usuario = id_usuario;
  }

  public int getId_noticia() {
    return id_noticia;
  }

  public void setId_noticia(int id_noticia) {
    this.id_noticia = id_noticia;
  }

  public String getFechaFavorito() {
    return fechaFavorito;
  }

  public void setFechaFavorito(String fechaFavorito) {
    this.fechaFavorito = fechaFavorito;
  }

  //metodo que entrega favorito como string con sus atributos.
  @Override
  public String toString() {
    return "Favorito{" +
        "id=" + id +
        ", idUsuario=" + id_usuario +
        ", idNoticia=" + id_noticia +
        ", fechaFavorito='" + fechaFavorito + '\'' +
        '}';
  }
}
