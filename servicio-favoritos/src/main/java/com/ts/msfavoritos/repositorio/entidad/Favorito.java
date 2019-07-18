package com.ts.msfavoritos.repositorio.entidad;


import javax.persistence.*;

@Entity
@Table(name = "favorito")
public class Favorito {

    @Id
    @GeneratedValue
    @Column(name = "id_favorito")
    private int id_favorito;

    @Column(name = "id_noticia")
    private int id_noticia;

    @Column(name = "id_usuario")
    private int id_usuario;

    @Column(name = "fecha_favorito")
    private String fecha_favorito;

    public Favorito() {
    }

    public Favorito(int id_noticia, int id_usuario, String fecha_favorito) {
        this.id_noticia = id_noticia;
        this.id_usuario = id_usuario;
        this.fecha_favorito = fecha_favorito;
    }

    public int getId_favorito() {
        return id_favorito;
    }

    public void setId_favorito(int id_favorito) {
        this.id_favorito = id_favorito;
    }

    public int getId_noticia() {
        return id_noticia;
    }

    public void setId_noticia(int id_noticia) {
        this.id_noticia = id_noticia;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getFecha_favorito() {
        return fecha_favorito;
    }

    public void setFecha_favorito(String fecha_favorito) {
        this.fecha_favorito = fecha_favorito;
    }

    @Override
    public String toString() {
        return "Favorito{" +
                "id_favorito=" + id_favorito +
                ", id_noticia=" + id_noticia +
                ", id_usuario=" + id_usuario +
                ", fecha_favorito='" + fecha_favorito + '\'' +
                '}';
    }
}
