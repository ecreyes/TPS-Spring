package com.tps.msnoticias.service.imp;

import com.tps.msnoticias.repository.NoticiaJpaRepository;
import com.tps.msnoticias.repository.entity.Noticia;
import com.tps.msnoticias.service.NoticiaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("noticiaService")
public class NoticiaServiceImp implements NoticiaService {

    private final NoticiaJpaRepository noticiaJpaRepository;

    public NoticiaServiceImp(@Qualifier("noticiaJpaRepository") NoticiaJpaRepository noticiaJpaRepository) {
        this.noticiaJpaRepository = noticiaJpaRepository;
    }

	@Override
	public List<Noticia> getNoticias() {
		return noticiaJpaRepository.findAll();
	}

	@Override
	public Noticia getNoticia(int id) {
		Noticia noticia = noticiaJpaRepository.getOne(id);
		return noticia;
	}

	@Override
	public Noticia agregarNoticia(Noticia noticia) {
		return noticiaJpaRepository.save(noticia);
	}

	@Override
	public int eliminarNoticia(int id) {
		try{
			noticiaJpaRepository.deleteById(id);
			return 1;
		}catch(Exception e){
			return 0;
		}
	}


	@Override
	public Noticia editarNoticia(Noticia noticia) {
		return noticiaJpaRepository.save(noticia);
	}
}
