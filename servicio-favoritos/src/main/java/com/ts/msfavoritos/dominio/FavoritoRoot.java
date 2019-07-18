package com.ts.msfavoritos.dominio;

public class FavoritoRoot {

    private int id;
    private UsuarioIdVO usuarioIdVO;
    private NoticiaIdVO noticiaIdVO;
    private String fechaFavorito;

    //Constructor de eliminacion
    public FavoritoRoot(UsuarioIdVO usuarioIdVO, NoticiaIdVO noticiaIdVO) {
        this.usuarioIdVO = usuarioIdVO;
        this.noticiaIdVO = noticiaIdVO;
    }

    //Constructor de creacion
    public FavoritoRoot(UsuarioIdVO usuarioIdVO, NoticiaIdVO noticiaIdVO, String fechaFavorito) {
        this.usuarioIdVO = usuarioIdVO;
        this.noticiaIdVO = noticiaIdVO;
        this.fechaFavorito = fechaFavorito;
    }

    //Constructor para consulta de favs
    public FavoritoRoot(int id, UsuarioIdVO usuarioIdVO, NoticiaIdVO noticiaIdVO, String fechaFavorito) {
        this.id = id;
        this.usuarioIdVO = usuarioIdVO;
        this.noticiaIdVO = noticiaIdVO;
        this.fechaFavorito = fechaFavorito;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsuarioIdVO getUsuarioIdVO() {
        return usuarioIdVO;
    }

    public void setUsuarioIdVO(UsuarioIdVO usuarioIdVO) {
        this.usuarioIdVO = usuarioIdVO;
    }

    public NoticiaIdVO getNoticiaIdVO() {
        return noticiaIdVO;
    }

    public void setNoticiaIdVO(NoticiaIdVO noticiaIdVO) {
        this.noticiaIdVO = noticiaIdVO;
    }

    public String getFechaFavorito() {
        return fechaFavorito;
    }

    public void setFechaFavorito(String fechaFavorito) {
        this.fechaFavorito = fechaFavorito;
    }

    @Override
    public String toString() {
        return "FavoritoRoot{" +
                "id=" + id +
                ", usuarioIdVO=" + usuarioIdVO +
                ", noticiaIdVO=" + noticiaIdVO +
                ", fechaFavorito='" + fechaFavorito + '\'' +
                '}';
    }
}
