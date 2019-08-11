package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Usuario;

public interface UsuarioMsg {

  void enviarMsg(Usuario usuario, String routeKey);

  String solicitarLogin(Usuario usuario);
}
