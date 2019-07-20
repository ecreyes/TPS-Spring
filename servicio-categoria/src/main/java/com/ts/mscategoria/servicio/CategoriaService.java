package com.ts.mscategoria.servicio;


import com.ts.mscategoria.dominio.CategoriaRoot;

import java.util.List;

public interface CategoriaService {

    List<CategoriaRoot> obtenerCategorias();

    void agregar(CategoriaRoot categoriaRoot);

    void eliminar(CategoriaRoot categoriaRoot);

    void editar(CategoriaRoot categoriaRoot);
}
