package com.ts.mscategoria.dominio;

public class CategoriaVO {

    private final String nombre;
    private final int id;

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
