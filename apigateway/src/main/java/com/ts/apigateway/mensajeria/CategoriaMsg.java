package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Categoria;

import java.util.List;

public interface CategoriaMsg {

  void enviarMsg(Categoria categoria, String routeKey);

  List<Categoria> obtenerListaCategorias();
}
