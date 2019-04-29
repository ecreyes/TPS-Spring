package com.tps.msnoticias.controller;

import com.tps.msnoticias.dominio.NoticiaRoot;
import com.tps.msnoticias.repository.entity.Noticia;
import com.tps.msnoticias.repository.service.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NoticiaAPI {

	@Autowired
	@Qualifier("noticiaServiceImp")
	private NoticiaService noticiaService;

	/**
	 * Funcion que retorna una lista de noticias root y que mapea una solicitud get de noticias
	 * @return Lista de Noticias Root
	 */
	@GetMapping("/noticias")
	public List<NoticiaRoot> getNoticias() {
		List<NoticiaRoot> noticias = new ArrayList<>();
		List<Noticia> noticiasDB = noticiaService.getNoticias();
		for (int i = 0; i < noticiasDB.size(); i++) {
			String titular = noticiasDB.get(i).getTitular();
			String descripcion = noticiasDB.get(i).getDescripcion();
			String autor = noticiasDB.get(i).getAutor();
			String url = noticiasDB.get(i).getUrl();
			String fuente = noticiasDB.get(i).getFuente();
			noticias.add(new NoticiaRoot(titular, descripcion, autor, url, fuente));
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

}
