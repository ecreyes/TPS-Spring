package com.ts.msfavoritos.servicio;

import com.ts.msfavoritos.dominio.FavoritoRoot;

public interface FavoritoService {

    void agregarFavorito(FavoritoRoot favorito);

    int eliminarFavorito(FavoritoRoot favorito);
}
