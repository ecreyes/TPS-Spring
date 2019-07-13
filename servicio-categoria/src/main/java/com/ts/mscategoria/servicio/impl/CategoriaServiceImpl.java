package com.ts.mscategoria.servicio.impl;


import com.ts.mscategoria.dominio.CategoriaList;
import com.ts.mscategoria.dominio.CategoriaVO;
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
    private CategoriaList agregado = null;

    private static final Log LOGGER = LogFactory.getLog(CategoriaServiceImpl.class);

    public CategoriaServiceImpl(@Qualifier("categoriaJpaRepository") CategoriaJpaRepository categoriaJpaRepository) {
        this.categoriaJpaRepository = categoriaJpaRepository;
    }

    @Override
    public CategoriaList obtenerAgregado() {
        return agregado;
    }

    /**
     * Funcion encargada de cargar el agregado categoria en memoria con listado de value objects
     */
    @Override
    public void cargarAgregado(boolean actualizar) {

        if (agregado == null || actualizar) {
            //Listado de entidades (BD)
            List<Categoria> categoriaList = obtenerCategorias();

            //Listado de value objects
            List<CategoriaVO> categoriaVOList = new ArrayList<>();

            for (Categoria categoria : categoriaList) {

                //Transformar entidad a VO
                CategoriaVO categoriaVO = new CategoriaVO(categoria.getId(), categoria.getNombre());

                categoriaVOList.add(categoriaVO);
            }

            agregado = new CategoriaList(categoriaVOList);

            LOGGER.info("CREANDO - ACTUALIZANDO AGREGADO CATEGORIA");
        }
    }

    /**
     * Funcion encargada de buscar categorias desde base de datos
     *
     * @return Listado de entidades categorias
     */
    private List<Categoria> obtenerCategorias() {
        return categoriaJpaRepository.findAll();
    }

    @Override
    public void agregarCategoria(Categoria categoria) {
        categoriaJpaRepository.save(categoria);
    }

    //TODO: Pendiente por ejecutar
    @Override
    public int eliminarCategoria() {
        return 0;
    }

    @Override
    public Categoria editarCategoria(CategoriaVO editCatVO) {

        Categoria categoria = null;

        //si la categoria fue encontrada en BD
        if (categoriaJpaRepository.findById(editCatVO.getId()).isPresent()) {

            //Buscar categoria en BD
            categoria = categoriaJpaRepository.findById(editCatVO.getId()).get();

            //Actualizar datos
            categoria.setNombre(editCatVO.getNombre());

            categoriaJpaRepository.save(categoria);
        }
        return categoria;
    }
}
