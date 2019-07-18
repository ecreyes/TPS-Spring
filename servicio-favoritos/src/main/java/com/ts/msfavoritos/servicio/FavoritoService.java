package com.ts.msfavoritos.servicio;

import com.ts.msfavoritos.dominio.FavoritoRoot;

import java.util.List;

public interface FavoritoService {

    void agregar(FavoritoRoot favorito);

    int eliminar(FavoritoRoot favorito);

    List<FavoritoRoot> getListaFavUsuario(int id_usuario);
}
