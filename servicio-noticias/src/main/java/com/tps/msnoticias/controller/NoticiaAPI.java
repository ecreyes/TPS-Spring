package com.tps.msnoticias.controller;

import com.tps.msnoticias.dominio.NoticiaRoot;
import com.tps.msnoticias.repository.entity.Noticia;
import com.tps.msnoticias.service.NoticiaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NoticiaAPI {

    private final NoticiaService noticiaService;

    public NoticiaAPI(@Qualifier("noticiaService") NoticiaService noticiaService) {
        this.noticiaService = noticiaService;
    }

	/**
	 * Funcion que retorna una lista de noticias root y que mapea una solicitud get de noticias
	 * @return Lista de Noticias Root
	 */
	@GetMapping("/noticias")
	public List<NoticiaRoot> getNoticias() {
		List<NoticiaRoot> noticias = new ArrayList<>();
		List<Noticia> noticiasDB = noticiaService.getNoticias();
		for (int i = 0; i < noticiasDB.size(); i++) {
			int id = noticiasDB.get(i).getId();
			String titular = noticiasDB.get(i).getTitular();
			String descripcion = noticiasDB.get(i).getDescripcion();
			String autor = noticiasDB.get(i).getAutor();
			String url = noticiasDB.get(i).getUrl();
			String fuente = noticiasDB.get(i).getFuente();
			noticias.add(new NoticiaRoot(id,titular, descripcion, autor, url, fuente));
		}
		return noticias;
	}


	/*
	HEADER
	Content-Type  application/json
	BODY
	{
		"titular":"titular2",
		"descripcion":"descripcion2",
		"autor":"autor2",
		"url":"urlNoticia2",
		"fuente":"fuente2"
	}
	 */

	/**
	 * Funcion que se encarga de una solicitud POST para agregar noticias al sistema
	 * @param noticia Entidad noticia desde metodo POST
	 * @return Noticia , objecto que contiene la noticia recien ingresada
	 */
	@PostMapping("/noticia")
	public Noticia agregarNoticia(@RequestBody Noticia noticia) {
		noticiaService.agregarNoticia(noticia);
		return noticia;
	}

	@PutMapping("/noticia/{id}")
	public NoticiaRoot editarNoticia(@PathVariable int id,@RequestBody Noticia noticia){
		NoticiaRoot noticiaRoot = new NoticiaRoot();
		Noticia mNoticia = noticiaService.getNoticia(id);
		noticiaRoot = new NoticiaRoot(mNoticia.getId(),mNoticia.getTitular(),
				mNoticia.getDescripcion(),mNoticia.getAutor(),mNoticia.getUrl(),mNoticia.getFuente());
		if(noticia.getAutor()!=null){
			noticiaRoot.setAutor(noticia.getAutor());
			mNoticia.setAutor(noticia.getAutor());
		}
		if(noticia.getTitular()!=null){
			noticiaRoot.setTitular(noticia.getTitular());
			mNoticia.setTitular(noticia.getTitular());
		}
		if(noticia.getDescripcion()!=null){
			noticiaRoot.setDescripcion(noticia.getDescripcion());
			mNoticia.setDescripcion(noticia.getDescripcion());
		}
		if(noticia.getFuente()!=null){
			noticiaRoot.setFuente(noticia.getFuente());
			mNoticia.setFuente(noticia.getFuente());
		}
		if(noticia.getUrl()!=null){
			noticiaRoot.setUrl(noticia.getUrl());
			mNoticia.setUrl(noticia.getUrl());
		}
		mNoticia.setId(id);
		noticiaRoot.setId(id);
		noticiaService.editarNoticia(mNoticia);
		return noticiaRoot;
	}

	@GetMapping("/noticia/{id}")
	public NoticiaRoot getNoticia(@PathVariable int id){
		Noticia noticia = noticiaService.getNoticia(id);
		NoticiaRoot noticiaRoot= new NoticiaRoot(noticia.getId(),noticia.getTitular(),
				noticia.getDescripcion(),noticia.getAutor(),noticia.getUrl(),noticia.getFuente());
		return noticiaRoot;
	}

	@DeleteMapping("/noticia/{id}")
	public boolean eliminarNoticia(@PathVariable int id){
		if(noticiaService.eliminarNoticia(id)==1){
			return true;
		}else{
			return false;
		}
	}

}
