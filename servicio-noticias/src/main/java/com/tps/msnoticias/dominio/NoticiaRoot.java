package com.tps.msnoticias.dominio;

public class NoticiaRoot {

    private int id;
    private String titular;
    private String descripcion;
    private String autor;
    private String url;
    private FuenteNoticiaVO fuenteNoticiaVO;

    public NoticiaRoot() {
    }

    //Contructor para edicion
    public NoticiaRoot(int id, String titular, String descripcion, String autor, String url,
                       FuenteNoticiaVO fuenteNoticiaVO) {
        this.id = id;
        this.titular = titular;
        this.descripcion = descripcion;
        this.autor = autor;
        this.url = url;
        this.fuenteNoticiaVO = fuenteNoticiaVO;
    }

    //Constructor para creacion
    public NoticiaRoot(String titular, String descripcion, String autor, String url, FuenteNoticiaVO fuenteNoticiaVO) {
        this.titular = titular;
        this.descripcion = descripcion;
        this.autor = autor;
        this.url = url;
        this.fuenteNoticiaVO = fuenteNoticiaVO;
    }

    //Constructor para eliminacion
    public NoticiaRoot(int id) {
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FuenteNoticiaVO getFuenteNoticiaVO() {
        return fuenteNoticiaVO;
    }

    public void setFuenteNoticiaVO(FuenteNoticiaVO fuenteNoticiaVO) {
        this.fuenteNoticiaVO = fuenteNoticiaVO;
    }

    @Override
    public String toString() {
        return "NoticiaRoot{" +
                "id=" + id +
                ", titular='" + titular + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", autor='" + autor + '\'' +
                ", url='" + url + '\'' +
                ", fuenteNoticiaVO=" + fuenteNoticiaVO +
                '}';
    }
}
