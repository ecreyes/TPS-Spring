package com.ts.msfavoritos.servicio;

import com.ts.msfavoritos.dominio.FavoritoRoot;
import com.ts.msfavoritos.repositorio.FavoritoJpaRepository;
import com.ts.msfavoritos.repositorio.entidad.Favorito;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("favoritoService")
public class FavoritoServiceImpl implements FavoritoService {

  private final FavoritoJpaRepository favoritoJpaRepository;

  public FavoritoServiceImpl(@Qualifier("favoritoJpaRepository") FavoritoJpaRepository repository) {
    this.favoritoJpaRepository = repository;
  }

  @Override
  public void agregar(FavoritoRoot favoritoRoot) {
    Favorito favorito = new Favorito(favoritoRoot.getNoticiaIdVO().getId(),
        favoritoRoot.getUsuarioIdVO().getId()
        , favoritoRoot.getFechaFavorito());
    favoritoJpaRepository.save(favorito);
  }

  @Override
  public void eliminar(FavoritoRoot favoritoRoot) {

    if (favoritoJpaRepository.findByIdUsuarioAndIdNoticia(favoritoRoot.getUsuarioIdVO().getId(),
        favoritoRoot.getNoticiaIdVO().getId()).isPresent()) {

      favoritoJpaRepository.deleteByIdUsuarioAndIdNoticia(favoritoRoot.getUsuarioIdVO().getId(),
          favoritoRoot.getNoticiaIdVO().getId());
    }
  }

  /**
   * Funcion que busca en BD los favoritos de un usuario
   *
   * @param idUsuario Necesario para busqueda
   * @return Listado de agregados de favoritos
   */
  @Override
  public List<FavoritoRoot> getListaFavUsuario(int idUsuario) {

    List<FavoritoRoot> favoritoRootList = new ArrayList<>();

    //Favoritos usuario desde BD
    List<Favorito> favoritoList = favoritoJpaRepository.findAllByIdUsuario(idUsuario);

    for (Favorito favorito : favoritoList) {

      FavoritoRoot favoritoRoot = new FavoritoRoot(favorito.getId(), favorito.getIdUsuario(),
          favorito.getIdNoticia(),
          favorito.getFechaFavorito());

      favoritoRootList.add(favoritoRoot);
    }
    return favoritoRootList;
  }
}
