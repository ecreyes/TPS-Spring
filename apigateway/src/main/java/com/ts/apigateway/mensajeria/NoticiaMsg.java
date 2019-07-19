package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Noticia;

import java.util.List;

public interface NoticiaMsg {

    void send(Noticia noticia, String route_key);

    List<Noticia> getList();
}
