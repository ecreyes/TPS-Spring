package com.ts.msfavoritos.dominio;
/**Root de Favorito**/
public class FavoritoRoot {
  //atributos de la clase favoritoRoot
  private int id;
  private UsuarioIdVO usuarioIdVO;
  private NoticiaIdVO noticiaIdVO;
  private String fechaFavorito;

  //Constructor de eliminacion
  public FavoritoRoot(int usuarioIdVO, int noticiaIdVO) {
    this.usuarioIdVO = new UsuarioIdVO(usuarioIdVO);
    this.noticiaIdVO = new NoticiaIdVO(noticiaIdVO);
  }

  //Constructor de creacion
  public FavoritoRoot(int usuarioIdVO, int noticiaIdVO, String fechaFavorito) {
    this.usuarioIdVO = new UsuarioIdVO(usuarioIdVO);
    this.noticiaIdVO = new NoticiaIdVO(noticiaIdVO);
    this.fechaFavorito = fechaFavorito;
  }

  //Constructor para consulta de favs
  public FavoritoRoot(int id, int usuarioIdVO, int noticiaIdVO, String fechaFavorito) {
    this.id = id;
    this.usuarioIdVO = new UsuarioIdVO(usuarioIdVO);
    this.noticiaIdVO = new NoticiaIdVO(noticiaIdVO);
    this.fechaFavorito = fechaFavorito;
  }

  //getters y setters
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

  //metodo que devuelve el favoritoRoot como string con sus atributos
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
