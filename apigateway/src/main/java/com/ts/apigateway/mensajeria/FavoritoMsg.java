package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Favorito;

public interface FavoritoMsg {

    void send(Favorito favorito, String route_key);
}
