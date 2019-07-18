package com.ts.msfavoritos.servicio.impl;

import com.ts.msfavoritos.dominio.FavoritoRoot;
import com.ts.msfavoritos.dominio.NoticiaIdVO;
import com.ts.msfavoritos.dominio.UsuarioIdVO;
import com.ts.msfavoritos.repositorio.FavoritoJpaRepository;
import com.ts.msfavoritos.repositorio.entidad.Favorito;
import com.ts.msfavoritos.servicio.FavoritoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("favoritoService")
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoJpaRepository favoritoJpaRepository;

    public FavoritoServiceImpl(@Qualifier("favoritoJpaRepository") FavoritoJpaRepository repository) {
        this.favoritoJpaRepository = repository;
    }

    @Override
    public void agregar(FavoritoRoot favoritoRoot) {
        Favorito favorito = new Favorito(favoritoRoot.getNoticiaIdVO().getId(), favoritoRoot.getUsuarioIdVO().getId()
                , favoritoRoot.getFechaFavorito());
        favoritoJpaRepository.save(favorito);
    }

    @Override
    public int eliminar(FavoritoRoot favoritoRoot) {

        if (favoritoJpaRepository.findById_usuarioAndId_noticia(favoritoRoot.getUsuarioIdVO().getId(),
                favoritoRoot.getNoticiaIdVO().getId()).isPresent()) {

            favoritoJpaRepository.deleteById_usuarioAndId_noticia(favoritoRoot.getUsuarioIdVO().getId(),
                    favoritoRoot.getNoticiaIdVO().getId());

            return 1;
        }
        return 0;
    }

    @Override
    public List<FavoritoRoot> getUserFavList(int id_usuario) {

        List<FavoritoRoot> favoritoRootList = new ArrayList<>();

        //Favoritos usuario desde BD
        List<Favorito> favoritoList = favoritoJpaRepository.findAllById_usuario(id_usuario);

        for (Favorito favorito : favoritoList) {
            UsuarioIdVO usuarioIdVO = new UsuarioIdVO(favorito.getId_usuario());
            NoticiaIdVO noticiaIdVO = new NoticiaIdVO(favorito.getId_noticia());

            System.out.println(favorito.getId());
            FavoritoRoot favoritoRoot = new FavoritoRoot(favorito.getId(), usuarioIdVO, noticiaIdVO,
                    favorito.getFecha_favorito());

            favoritoRootList.add(favoritoRoot);
        }

        return favoritoRootList;
    }


}
