package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Categoria;

import java.util.List;
/*
Interface para describir los métodos
que se utilizaran para enviar o recibir un mensaje
correspondiente a la categoria.
 */
public interface CategoriaMsg {

  void enviarMsg(Categoria categoria, String routeKey);

  List<Categoria> obtenerListaCategorias();
}
