package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Usuario;

public interface UsuarioMsg {

    void enviarMsg(Usuario usuario, String route_key);

    String solicitarLogin(Usuario usuario);
}
