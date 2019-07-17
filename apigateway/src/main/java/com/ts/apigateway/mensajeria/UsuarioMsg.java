package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Usuario;

public interface UsuarioMsg {

    void send(Usuario usuario, String route_key);

    String requestLogin(Usuario usuario);
}
