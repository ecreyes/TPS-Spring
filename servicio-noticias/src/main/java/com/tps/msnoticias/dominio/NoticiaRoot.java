package com.tps.msnoticias.dominio;

public class NoticiaRoot {

  private int id;
  private String titular;
  private String descripcion;
  private String autor;
  private String url;
  private FuenteNoticiaVO fuenteNoticiaVO;
  private CategoriaNoticiaVO categoriaNoticiaVO;

  public NoticiaRoot() {
  }

  //Contructor para edicion
  public NoticiaRoot(int id, String titular, String descripcion, String autor, String url,
      String fuenteNoticiaVO, int id_categoria) {
    this.id = id;
    this.titular = titular;
    this.descripcion = descripcion;
    this.autor = autor;
    this.url = url;
    this.fuenteNoticiaVO = new FuenteNoticiaVO(fuenteNoticiaVO);
    this.categoriaNoticiaVO = new CategoriaNoticiaVO(id_categoria);
  }

  //Constructor para creacion
  public NoticiaRoot(String titular, String descripcion, String autor, String url,
      String fuenteNoticiaVO,
      int id_categoria) {
    this.titular = titular;
    this.descripcion = descripcion;
    this.autor = autor;
    this.url = url;
    this.fuenteNoticiaVO = new FuenteNoticiaVO(fuenteNoticiaVO);
    this.categoriaNoticiaVO = new CategoriaNoticiaVO(id_categoria);
  }

  //Constructor para get Categorias
  public NoticiaRoot(int id, String titular, String descripcion, String autor, String url,
      String fuenteNoticiaVO,
      int id_categoria, String nombre_categoria) {
    this.id = id;
    this.titular = titular;
    this.descripcion = descripcion;
    this.autor = autor;
    this.url = url;
    this.fuenteNoticiaVO = new FuenteNoticiaVO(fuenteNoticiaVO);
    this.categoriaNoticiaVO = new CategoriaNoticiaVO(id_categoria, nombre_categoria);
  }

  //Constructor para eliminacion
  public NoticiaRoot(int id) {
    this.id = id;
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

  public CategoriaNoticiaVO getCategoriaNoticiaVO() {
    return categoriaNoticiaVO;
  }

  public void setCategoriaNoticiaVO(CategoriaNoticiaVO categoriaNoticiaVO) {
    this.categoriaNoticiaVO = categoriaNoticiaVO;
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
        ", categoriaNoticiaVO=" + categoriaNoticiaVO +
        '}';
  }
}
