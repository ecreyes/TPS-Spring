package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Favorito;

import java.util.List;

public interface FavoritoMsg {

    void send(Favorito favorito, String route_key);

    List<Favorito> getFavList(String id_usuario);
}
