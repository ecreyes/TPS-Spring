package com.tps.msnoticias.service;

import com.tps.msnoticias.dominio.NoticiaRoot;
import com.tps.msnoticias.repository.entity.Noticia;

import java.util.List;

public interface NoticiaService {
	List<NoticiaRoot> getNoticias();

	Noticia getNoticia(int id);

	void agregarNoticia(NoticiaRoot noticiaRoot);

	int eliminarNoticia(int id);

	void editarNoticia(NoticiaRoot noticiaRoot);
}
