package com.tps.msusuario.repositorio.entidad;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private int id;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "nombreUsuario")
  private String nombreUsuario;

  @Column(name = "estado")
  private String estado;

  public Usuario() {
  }

  public Usuario(String email, String password, String nombreUsuario, String estado) {
    this.email = email;
    this.password = password;
    this.nombreUsuario = nombreUsuario;
    this.estado = estado;
  }

  public int getId() {
    return id;
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

  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  @Override
  public String toString() {
    return "Usuario{" +
        "id=" + id +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", nombreUsuario='" + nombreUsuario + '\'' +
        ", estado='" + estado + '\'' +
        '}';
  }
}
