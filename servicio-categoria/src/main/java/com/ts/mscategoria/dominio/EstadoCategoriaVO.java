package com.ts.mscategoria.dominio;

/**Value Object para obtener el estado de la categoria**/
public class EstadoCategoriaVO {
  //atributo de estado del value object
  private String estado;

  EstadoCategoriaVO(String estado) {
    this.estado = estado;
  }

  public String getEstado() {
    return estado;
  }
}
