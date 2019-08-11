package com.tps.msusuario.dominio;

public class UsuarioRoot {

  private int id;
  private NombreUsuarioVO nombreUsuarioVO;
  private String email;
  private String password;
  private EstadoUsuarioVO estadoUsuarioVO;

  //Contructor editar
  public UsuarioRoot(int id, String nombreUsuarioVO, String email, String password,
      String estadoUsuarioVO) {
    this.id = id;
    this.nombreUsuarioVO = new NombreUsuarioVO(nombreUsuarioVO);
    this.email = email;
    this.password = password;
    this.estadoUsuarioVO = new EstadoUsuarioVO(estadoUsuarioVO);
  }

  //Contructor crear
  public UsuarioRoot(String nombreUsuarioVO, String email, String password,
      String estadoUsuarioVO) {
    this.nombreUsuarioVO = new NombreUsuarioVO(nombreUsuarioVO);
    this.email = email;
    this.password = password;
    this.estadoUsuarioVO = new EstadoUsuarioVO(estadoUsuarioVO);
  }

  //Constructor login
  public UsuarioRoot(int id, String nombreUsuarioVO, String email,
      String estadoUsuarioVO) {
    this.id = id;
    this.nombreUsuarioVO = new NombreUsuarioVO(nombreUsuarioVO);
    this.email = email;
    this.estadoUsuarioVO = new EstadoUsuarioVO(estadoUsuarioVO);
  }

  //Contructor para eliminar
  public UsuarioRoot(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public NombreUsuarioVO getNombreUsuarioVO() {
    return nombreUsuarioVO;
  }

  public void setNombreUsuarioVO(NombreUsuarioVO nombreUsuarioVO) {
    this.nombreUsuarioVO = nombreUsuarioVO;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public EstadoUsuarioVO getEstadoUsuarioVO() {
    return estadoUsuarioVO;
  }

  public void setEstadoUsuarioVO(EstadoUsuarioVO estadoUsuarioVO) {
    this.estadoUsuarioVO = estadoUsuarioVO;
  }

  @Override
  public String toString() {
    return "UsuarioRoot{" +
        "id=" + id +
        ", nombreUsuarioVO=" + nombreUsuarioVO +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", estadoUsuarioVO=" + estadoUsuarioVO +
        '}';
  }
}
