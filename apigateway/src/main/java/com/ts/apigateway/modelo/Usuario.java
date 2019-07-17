package com.ts.apigateway.modelo;

public class Usuario {

    private int id;
    private String email;
    private String password;
    private String username;
    private String estado;

    public Usuario() {
    }

    public Usuario(int id, String email, String password, String username, String estado) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
                ", username='" + username + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
