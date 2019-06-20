package com.ts.apigateway.modelo;

public class Favorito {

    private int id_usuario;
    private int id_noticia;

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

    public Favorito() {
    }

    public Favorito(int id_usuario, int id_noticia) {
        this.id_usuario = id_usuario;
        this.id_noticia = id_noticia;
    }

    @Override
    public String toString() {
        return "Favorito{" +
                "id_usuario=" + id_usuario +
                ", id_noticia=" + id_noticia +
                '}';
    }
}
