package com.ts.msfavoritos.repositorio.servicio;

import com.ts.msfavoritos.repositorio.entidad.Categoria;

public interface CategoriaService {

	public abstract Categoria agregarCategoria();

	public abstract int eliminarCategoria();

	public abstract Categoria editarCategoria();
}
