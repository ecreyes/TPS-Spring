package com.ts.msfavoritos.servicio;

import com.ts.msfavoritos.repositorio.FavoritoJpaRepository;
import com.ts.msfavoritos.repositorio.entidad.Favorito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("favoritoService")
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoJpaRepository favoritoJpaRepository;

    public FavoritoServiceImpl(@Qualifier("favoritoJpaRepository") FavoritoJpaRepository repository) {
        this.favoritoJpaRepository = repository;
    }

    @Override
    public Favorito agregarFavorito(Favorito favorito) {
        favorito=favoritoJpaRepository.save(favorito);
        return favorito;
    }

    @Override
    public int eliminarFavorito(Favorito favorito) {
        return 0;
    }

    @Override
    public Favorito editarFavorito(Favorito favorito) {
        return null;
    }
}
