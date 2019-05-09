package com.ts.mscategoria.servicio;


import com.ts.mscategoria.repositorio.entidad.Categoria;

import java.util.List;

public interface CategoriaService {

	public abstract List<Categoria> obtenerCategorias();

	public abstract Categoria agregarCategoria(Categoria categoria);

	public abstract int eliminarCategoria();

	public abstract Categoria editarCategoria();
}
