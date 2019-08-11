package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Noticia;

import java.util.List;

public interface NoticiaMsg {

  void enviarMsg(Noticia noticia, String routeKey);

  List<Noticia> obtenerListadoNoticias();
}
