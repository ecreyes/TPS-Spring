package com.tps.msusuario.servicio;

import com.tps.msusuario.dominio.UsuarioRoot;

import java.util.Map;

public interface UsuarioService {

    void agregar(UsuarioRoot usuario);

    int eliminar(UsuarioRoot usuarioRoot);

    void editar(UsuarioRoot usuarioRoot);

    Map<String, Object> login(UsuarioRoot usuarioRoot);

}
