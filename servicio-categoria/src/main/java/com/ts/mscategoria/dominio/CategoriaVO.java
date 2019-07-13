package com.ts.mscategoria.dominio;

public class CategoriaVO {

    private String nombre;
    private int id;

    public CategoriaVO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "CategoriaVO{" +
                "nombre='" + nombre + '\'' +
                ", id=" + id +
                '}';
    }
}
