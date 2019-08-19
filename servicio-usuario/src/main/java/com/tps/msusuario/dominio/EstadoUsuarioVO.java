package com.tps.msusuario.dominio;

/**Clase para representar el estado de un usuario como value object*/
public class EstadoUsuarioVO {

  private String estado;

  EstadoUsuarioVO(String estado) {
    this.estado = estado;
  }

  public String getEstado() {
    return estado;
  }
}
