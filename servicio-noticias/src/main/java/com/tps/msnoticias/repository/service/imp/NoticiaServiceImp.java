package com.tps.msnoticias.repository.service.imp;

import com.tps.msnoticias.repository.service.NoticiaService;
import com.tps.msnoticias.repository.NoticiaJpaRepository;
import com.tps.msnoticias.repository.entity.Noticia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("noticiaServiceImp")
public class NoticiaServiceImp implements NoticiaService {
    @Autowired
    @Qualifier("noticiaJpaRepository")
    private NoticiaJpaRepository noticiaJpaRepository;

    @Override
    public List<Noticia> getNoticias() {
        return noticiaJpaRepository.findAll();
    }

    @Override
    public Noticia agregarNoticia(Noticia noticia) {
        return noticiaJpaRepository.save(noticia);
    }

    @Override
    public int eliminarNoticia(Noticia noticia) {
        noticiaJpaRepository.delete(noticia);
        return 0;
    }

    @Override
    public Noticia editarNoticia(Noticia noticia) {
        return noticiaJpaRepository.save(noticia);
    }
}
