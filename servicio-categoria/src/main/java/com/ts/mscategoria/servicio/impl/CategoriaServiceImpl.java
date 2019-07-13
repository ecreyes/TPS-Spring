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
    private CategoriaList agregado=null;

    private static final Log LOGGER = LogFactory.getLog(CategoriaServiceImpl.class);

    public CategoriaServiceImpl(@Qualifier("categoriaJpaRepository") CategoriaJpaRepository categoriaJpaRepository) {
        this.categoriaJpaRepository = categoriaJpaRepository;
    }

    @Override
    public CategoriaList getAgregado() {
        return agregado;
    }

    /**
     * Funcion encargada de cargar el agregado categoria en memoria
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

    private List<Categoria> obtenerCategorias() {
        return categoriaJpaRepository.findAll();
    }

    @Override
    public void agregarCategoria(Categoria categoria) {
        categoriaJpaRepository.save(categoria);
    }

    @Override
    public int eliminarCategoria() {
        return 0;
    }

    @Override
    public Categoria editarCategoria(CategoriaVO categoriaVO) {
        return null;
    }
}
