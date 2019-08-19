package com.tps.msnoticias.servicio;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tps.msnoticias.dominio.NoticiaRoot;
import com.tps.msnoticias.repositorio.NoticiaJpaRepository;
import com.tps.msnoticias.repositorio.entidad.Noticia;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**Implementación de la interfaz NoticiaService**/
@Service("noticiaService")
public class NoticiaServiceImp implements NoticiaService {

  private final NoticiaJpaRepository noticiaJpaRepository;

  public NoticiaServiceImp(
      @Qualifier("noticiaJpaRepository") NoticiaJpaRepository noticiaJpaRepository) {
    this.noticiaJpaRepository = noticiaJpaRepository;
  }


  /**
   * Funcion encargada de obtener noticias desde base de datos
   *
   * @param categorias JSON con datos de categorias usados para completar atributos de noticias
   * @return Lista de Agregados noticias
   */
  @Override
  public List<NoticiaRoot> obtenerNoticias(String categorias) {

    JsonArray jsonArray = new JsonParser().parse(categorias).getAsJsonArray();

    //Noticias desde BD (entidades)
    List<Noticia> noticiaList = noticiaJpaRepository.findAll();

    //Noticias como agregados
    List<NoticiaRoot> noticiaRootList = new ArrayList<>();

    for (Noticia noticia : noticiaList) {

      NoticiaRoot noticiaRoot = null;
      for (int i = 0; i < jsonArray.size(); i++) {

        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

        if (noticia.getIdCategoria() == jsonObject.get("id").getAsInt()) {

          noticiaRoot = new NoticiaRoot(noticia.getId(), noticia.getTitular(),
              noticia.getDescripcion()
              , noticia.getAutor(), noticia.getUrl(), noticia.getFuente(),
              jsonObject.get("id").getAsInt(),
              jsonObject.get("nombre").getAsString());
        }
      }
      noticiaRootList.add(noticiaRoot);
    }
    return noticiaRootList;
  }

  @Override
  public void agregar(NoticiaRoot noticiaRoot) {

    Noticia noticia = new Noticia(noticiaRoot.getTitular(), noticiaRoot.getDescripcion(),
        noticiaRoot.getAutor(),
        noticiaRoot.getUrl(), noticiaRoot.getFuenteNoticiaVO().getFuente(),
        noticiaRoot.getCategoriaNoticiaVO().getId());

    noticiaJpaRepository.save(noticia);
  }

  @Override
  public void eliminar(NoticiaRoot noticiaRoot) {

    if (noticiaJpaRepository.findById(noticiaRoot.getId()).isPresent()) {
      noticiaJpaRepository.deleteById(noticiaRoot.getId());
    }
  }


  @Override
  public void editar(NoticiaRoot noticiaRoot) {

    if (noticiaJpaRepository.findById(noticiaRoot.getId()).isPresent()) {

      Noticia noticia = noticiaJpaRepository.findById(noticiaRoot.getId()).get();

      noticia.setAutor(noticiaRoot.getAutor());
      noticia.setFuente(noticiaRoot.getFuenteNoticiaVO().getFuente());
      noticia.setDescripcion(noticiaRoot.getDescripcion());
      noticia.setTitular(noticiaRoot.getTitular());
      noticia.setUrl(noticiaRoot.getUrl());
      noticia.setIdCategoria(noticiaRoot.getCategoriaNoticiaVO().getId());

      noticiaJpaRepository.save(noticia);
    }
  }
}
