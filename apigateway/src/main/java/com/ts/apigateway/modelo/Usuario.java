package com.ts.apigateway.modelo;
/*
Clase del usuario correspondiente al modelo
de negocio.
 */
public class Usuario {
  //id del usuario
  private int id;
  //correo del usuario
  private String email;
  //contraseña
  private String password;
  //nombre de usuario
  private String username;
  //estado, puede ser creado o editado
  private String estado;

  //constructor vacío
  public Usuario() {
  }
  //constructor para inicializar el usuario.
  public Usuario(int id, String email, String password, String username, String estado) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.username = username;
    this.estado = estado;
  }

  //obtener Id de usuario
  public int getId() {
    return id;
  }

  //setear Id de usuario
  public void setId(int id) {
    this.id = id;
  }

  //obtener email usuario
  public String getEmail() {
    return email;
  }

  //setear email de usuario
  public void setEmail(String email) {
    this.email = email;
  }

  //obtener password de usuario
  public String getPassword() {
    return password;
  }

  //setear el password de usuario
  public void setPassword(String password) {
    this.password = password;
  }

  //obtener nombre de usuario
  public String getUsername() {
    return username;
  }

  //setear nombre de usuario
  public void setUsername(String username) {
    this.username = username;
  }

  //Obtener estado de usuario
  public String getEstado() {
    return estado;
  }

  //setear estado de usuario
  public void setEstado(String estado) {
    this.estado = estado;
  }

  //retornar el objeto como un string con sus propiedades
  @Override
  public String toString() {
    return "Usuario{" +
        "id=" + id +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", username='" + username + '\'' +
        ", estado='" + estado + '\'' +
        '}';
  }
}
