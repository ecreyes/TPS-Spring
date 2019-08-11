package com.ts.apigateway.modelo;

public class Favorito {

  private int id;
  private int id_usuario;
  private int id_noticia;
  private String fechaFavorito;

  public Favorito(int id, int id_usuario, int id_noticia, String fechaFavorito) {
    this.id = id;
    this.id_usuario = id_usuario;
    this.id_noticia = id_noticia;
    this.fechaFavorito = fechaFavorito;
  }

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
