package com.tps.msnoticias.dominio;

public class CategoriaNoticiaVO {

    private int id;
    private String nombre;

    CategoriaNoticiaVO(int id) {
        this.id = id;
    }

    CategoriaNoticiaVO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
