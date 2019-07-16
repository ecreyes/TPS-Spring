package com.ts.mscategoria.dominio;

import java.util.List;

public class CategoriaList {

    private final List<CategoriaVO> categoriaVOList;

    public CategoriaList(List<CategoriaVO> categoriaVOList) {
        this.categoriaVOList = categoriaVOList;
    }

    public List<CategoriaVO> getCategoriaVOList() {
        return categoriaVOList;
    }
}
