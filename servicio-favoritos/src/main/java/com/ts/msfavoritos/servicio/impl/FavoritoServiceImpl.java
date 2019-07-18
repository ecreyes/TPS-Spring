package com.ts.msfavoritos.servicio.impl;

import com.ts.msfavoritos.dominio.FavoritoRoot;
import com.ts.msfavoritos.repositorio.FavoritoJpaRepository;
import com.ts.msfavoritos.repositorio.entidad.Favorito;
import com.ts.msfavoritos.servicio.FavoritoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("favoritoService")
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoJpaRepository favoritoJpaRepository;

    public FavoritoServiceImpl(@Qualifier("favoritoJpaRepository") FavoritoJpaRepository repository) {
        this.favoritoJpaRepository = repository;
    }

    @Override
    public void agregarFavorito(FavoritoRoot favoritoRoot) {
        Favorito favorito = new Favorito(favoritoRoot.getNoticiaIdVO().getId(), favoritoRoot.getUsuarioIdVO().getId()
                , favoritoRoot.getFechaFavorito());
        favoritoJpaRepository.save(favorito);
    }

    @Override
    public int eliminarFavorito(FavoritoRoot favoritoRoot) {

        if (favoritoJpaRepository.findById_usuarioAndId_noticia(favoritoRoot.getUsuarioIdVO().getId(),
                favoritoRoot.getNoticiaIdVO().getId()).isPresent()) {

            favoritoJpaRepository.deleteById_usuarioAndId_noticia(favoritoRoot.getUsuarioIdVO().getId(),
                    favoritoRoot.getNoticiaIdVO().getId());

            return 1;
        }
        return 0;
    }
}
