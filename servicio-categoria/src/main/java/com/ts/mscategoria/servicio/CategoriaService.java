package com.ts.mscategoria.servicio;


import com.ts.mscategoria.dominio.CategoriaList;
import com.ts.mscategoria.repositorio.entidad.Categoria;

public interface CategoriaService {

	void cargarAgregado(boolean actualizar);

	CategoriaList getAgregado();

	void agregarCategoria(Categoria categoria);

	int eliminarCategoria();

	Categoria editarCategoria();
}
