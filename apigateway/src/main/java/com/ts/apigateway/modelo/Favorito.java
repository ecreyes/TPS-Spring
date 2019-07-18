package com.ts.apigateway.modelo;

public class Favorito {

    private int id;
    private int id_usuario;
    private int id_noticia;
    private String fecha_favorito;

    public Favorito(int id, int id_usuario, int id_noticia, String fecha_favorito) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.id_noticia = id_noticia;
        this.fecha_favorito = fecha_favorito;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_noticia() {
        return id_noticia;
    }

    public void setId_noticia(int id_noticia) {
        this.id_noticia = id_noticia;
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
                "id_favorito=" + id +
                ", id_usuario=" + id_usuario +
                ", id_noticia=" + id_noticia +
                ", fecha_favorito='" + fecha_favorito + '\'' +
                '}';
    }
}
