package com.tps.msusuario.dominio;

/**Clase para representar el nombre de usuario como value object**/
public class NombreUsuarioVO {

  private String nombreUsuario;

  NombreUsuarioVO(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }
}
