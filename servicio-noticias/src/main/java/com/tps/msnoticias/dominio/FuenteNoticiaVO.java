package com.tps.msnoticias.dominio;

public class FuenteNoticiaVO {

    private String fuente;

    public FuenteNoticiaVO(String fuente) {
        this.fuente = fuente;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }
}
