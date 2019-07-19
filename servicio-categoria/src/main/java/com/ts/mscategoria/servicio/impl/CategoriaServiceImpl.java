package com.ts.mscategoria.servicio.impl;


import com.ts.mscategoria.dominio.CategoriaRoot;
import com.ts.mscategoria.repositorio.CategoriaJpaRepository;
import com.ts.mscategoria.repositorio.entidad.Categoria;
import com.ts.mscategoria.servicio.CategoriaService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("categoriaService")
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaJpaRepository categoriaJpaRepository;

    private static final Log LOGGER = LogFactory.getLog(CategoriaServiceImpl.class);

    public CategoriaServiceImpl(@Qualifier("categoriaJpaRepository") CategoriaJpaRepository categoriaJpaRepository) {
        this.categoriaJpaRepository = categoriaJpaRepository;
    }

    /**
     * Funcion encargada de buscar categorias desde base de datos
     *
     * @return Listado de entidades categorias
     */
    @Override
    public List<CategoriaRoot> getCategorias() {

        //Categorias BD
        List<Categoria> categoriaList = categoriaJpaRepository.findAll();

        List<CategoriaRoot> categoriaRootList = new ArrayList<>();

        for (Categoria categoria : categoriaList) {

            CategoriaRoot categoriaRoot = new CategoriaRoot(categoria.getId(), categoria.getNombre(),categoria.getEstado());

            categoriaRootList.add(categoriaRoot);
        }
        return categoriaRootList;
    }

    @Override
    public void agregar(CategoriaRoot categoriaRoot) {

        Categoria categoria = new Categoria(categoriaRoot.getNombre(),categoriaRoot.getEstado());
        categoriaJpaRepository.save(categoria);
    }

    @Override
    public void eliminar(CategoriaRoot categoriaRoot) {

        if (categoriaJpaRepository.findById(categoriaRoot.getId()).isPresent()) {
            categoriaJpaRepository.deleteById(categoriaRoot.getId());
        }
    }

    @Override
    public void editar(CategoriaRoot editCatVO) {

        //si la categoria fue encontrada en BD
        if (categoriaJpaRepository.findById(editCatVO.getId()).isPresent()) {

            //Buscar categoria en BD
            Categoria categoria = categoriaJpaRepository.findById(editCatVO.getId()).get();

            //Actualizar datos
            categoria.setNombre(editCatVO.getNombre());
            categoria.setEstado(editCatVO.getEstado());

            categoriaJpaRepository.save(categoria);
        }
    }
}
