package com.tps.msnoticias.service.imp;

import com.tps.msnoticias.dominio.FuenteNoticiaVO;
import com.tps.msnoticias.dominio.NoticiaRoot;
import com.tps.msnoticias.repository.NoticiaJpaRepository;
import com.tps.msnoticias.repository.entity.Noticia;
import com.tps.msnoticias.service.NoticiaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("noticiaService")
public class NoticiaServiceImp implements NoticiaService {

    private final NoticiaJpaRepository noticiaJpaRepository;

    public NoticiaServiceImp(@Qualifier("noticiaJpaRepository") NoticiaJpaRepository noticiaJpaRepository) {
        this.noticiaJpaRepository = noticiaJpaRepository;
    }

    @Override
    public List<NoticiaRoot> getNoticias() {

        //Noticias desde BD
        List<Noticia> noticiaList = noticiaJpaRepository.findAll();

        List<NoticiaRoot> noticiaRootList = new ArrayList<>();

        for (Noticia noticia : noticiaList) {

            FuenteNoticiaVO fuenteNoticiaVO = new FuenteNoticiaVO(noticia.getFuente());
            NoticiaRoot noticiaRoot = new NoticiaRoot(noticia.getId(), noticia.getTitular(), noticia.getDescripcion()
                    , noticia.getAutor(), noticia.getUrl(), fuenteNoticiaVO);

            noticiaRootList.add(noticiaRoot);
        }
        return noticiaRootList;
    }

    @Override
    public Noticia getNoticia(int id) {
        Noticia noticia = noticiaJpaRepository.getOne(id);
        return noticia;
    }

    @Override
    public void agregarNoticia(NoticiaRoot noticiaRoot) {

        Noticia noticia = new Noticia(noticiaRoot.getTitular(), noticiaRoot.getDescripcion(), noticiaRoot.getAutor(),
                noticiaRoot.getUrl(), noticiaRoot.getFuenteNoticiaVO().getFuente());

        noticiaJpaRepository.save(noticia);
    }

    @Override
    public int eliminarNoticia(int id) {
        try {
            noticiaJpaRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }


    @Override
    public void editarNoticia(NoticiaRoot noticiaRoot) {

        if (noticiaJpaRepository.findById(noticiaRoot.getId()).isPresent()) {

            Noticia noticia = noticiaJpaRepository.findById(noticiaRoot.getId()).get();

            noticia.setAutor(noticiaRoot.getAutor());
            noticia.setFuente(noticiaRoot.getFuenteNoticiaVO().getFuente());
            noticia.setDescripcion(noticiaRoot.getDescripcion());
            noticia.setTitular(noticiaRoot.getTitular());
            noticia.setUrl(noticiaRoot.getUrl());

            noticiaJpaRepository.save(noticia);
        }
    }
}
