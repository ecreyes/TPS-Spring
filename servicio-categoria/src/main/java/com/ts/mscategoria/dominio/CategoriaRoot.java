package com.ts.mscategoria.dominio;

public class CategoriaRoot {

    private int id;
    private String nombre;
    private EstadoCategoriaVO estadoCategoriaVO;

    //Constructor de creacion
    public CategoriaRoot(String nombre, EstadoCategoriaVO estadoCategoriaVO) {
        this.nombre = nombre;
        this.estadoCategoriaVO = estadoCategoriaVO;
    }

    //Constructor de edicion
    public CategoriaRoot(int id, String nombre, EstadoCategoriaVO estadoCategoriaVO) {
        this.id = id;
        this.nombre = nombre;
        this.estadoCategoriaVO = estadoCategoriaVO;
    }

    //Constructor de eliminacion
    public CategoriaRoot(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public EstadoCategoriaVO getEstadoCategoriaVO() {
        return estadoCategoriaVO;
    }

    public void setEstadoCategoriaVO(EstadoCategoriaVO estadoCategoriaVO) {
        this.estadoCategoriaVO = estadoCategoriaVO;
    }

    @Override
    public String toString() {
        return "CategoriaRoot{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", estadoCategoriaVO=" + estadoCategoriaVO +
                '}';
    }
}

