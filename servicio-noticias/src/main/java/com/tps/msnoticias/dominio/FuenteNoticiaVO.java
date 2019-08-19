package com.tps.msnoticias.dominio;
/**Fuente de noticia como value Object**/
public class FuenteNoticiaVO {
  //atributo de la fuente
  private String fuente;

  //constructor de la fuente
  FuenteNoticiaVO(String fuente) {
    this.fuente = fuente;
  }

  //geters y setters
  public String getFuente() {
    return fuente;
  }

  public void setFuente(String fuente) {
    this.fuente = fuente;
  }
}
