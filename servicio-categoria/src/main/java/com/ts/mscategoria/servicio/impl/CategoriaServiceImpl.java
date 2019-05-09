package com.ts.mscategoria.servicio.impl;


import com.ts.mscategoria.repositorio.CategoriaJpaRepository;
import com.ts.mscategoria.repositorio.entidad.Categoria;
import com.ts.mscategoria.servicio.CategoriaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("categoriaService")
public class CategoriaServiceImpl implements CategoriaService {

	private final CategoriaJpaRepository categoriaJpaRepository;

	public CategoriaServiceImpl(@Qualifier("categoriaJpaRepository") CategoriaJpaRepository categoriaJpaRepository) {
		this.categoriaJpaRepository = categoriaJpaRepository;
	}

	@Override
	public List<Categoria> obtenerCategorias() {
		return categoriaJpaRepository.findAll();
	}

	@Override
	public Categoria agregarCategoria(Categoria categoria) {
		return categoriaJpaRepository.save(categoria);
	}

	@Override
	public int eliminarCategoria() {
		return 0;
	}

	@Override
	public Categoria editarCategoria() {
		return null;
	}
}
