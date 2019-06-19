package com.tps.msnoticias.service;

import com.tps.msnoticias.repository.entity.Noticia;

import java.util.List;

public interface NoticiaService {
	public List<Noticia> getNoticias();

	public Noticia getNoticia(int id);

	public Noticia agregarNoticia(Noticia noticia);

	public int eliminarNoticia(int id);

	public Noticia editarNoticia(Noticia noticia);
}
