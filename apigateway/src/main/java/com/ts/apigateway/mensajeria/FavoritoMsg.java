package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Favorito;

import java.util.List;

public interface FavoritoMsg {

    void enviarMsg(Favorito favorito, String route_key);

    List<Favorito> obtenerListaFavoritosUsuario(String id_usuario);
}
