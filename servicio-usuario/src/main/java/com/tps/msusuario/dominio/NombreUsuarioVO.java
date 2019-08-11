package com.tps.msusuario.dominio;

public class NombreUsuarioVO {

  private String nombreUsuario;

  NombreUsuarioVO(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }
}
