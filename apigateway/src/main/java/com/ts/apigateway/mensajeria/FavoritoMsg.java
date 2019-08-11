package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Favorito;

import java.util.List;

public interface FavoritoMsg {

  void enviarMsg(Favorito favorito, String routeKey);

  List<Favorito> obtenerListaFavoritosUsuario(String id_usuario);
}
