package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Categoria;

import java.util.List;

public interface CategoriaMsg {

    void send(Categoria categoria, String route_key);

    List<Categoria> getList();
}
