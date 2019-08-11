package com.tps.msnoticias.servicio;

import com.tps.msnoticias.dominio.NoticiaRoot;
import com.tps.msnoticias.repositorio.entidad.Noticia;

import java.util.List;

public interface NoticiaService {

  List<NoticiaRoot> obtenerNoticias(String categorias);

  void agregar(NoticiaRoot noticiaRoot);

  void eliminar(NoticiaRoot noticiaRoot);

  void editar(NoticiaRoot noticiaRoot);
}
