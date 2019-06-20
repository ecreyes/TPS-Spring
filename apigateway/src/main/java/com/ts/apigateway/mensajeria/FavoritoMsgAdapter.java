package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Favorito;

public interface FavoritoMsgAdapter {

    void send(Favorito favorito);
}
