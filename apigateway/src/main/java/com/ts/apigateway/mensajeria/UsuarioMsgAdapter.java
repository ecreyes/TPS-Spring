package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Usuario;

public interface UsuarioMsgAdapter {

    void send(Usuario usuario);

    void requestLogin(Usuario usuario);
}
