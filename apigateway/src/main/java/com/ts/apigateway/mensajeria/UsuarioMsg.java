package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Usuario;
/**Interfaz que permite realizar la comunicación de NoticiasController con rabbit**/
public interface UsuarioMsg {

  void enviarMsg(Usuario usuario, String routeKey);

  String solicitarLogin(Usuario usuario);
}
