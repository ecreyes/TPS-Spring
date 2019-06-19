package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Usuario;

import java.util.Map;

public interface UsuarioMsgAdapter {

    void send(Usuario usuario);

    Map<String, Object> requestLogin(Usuario usuario);
}
