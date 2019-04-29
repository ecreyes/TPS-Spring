package com.ts.msfavoritos.repositorio.servicio;

import com.ts.msfavoritos.repositorio.entidad.Favorito;

public interface FavoritoService {

	public abstract Favorito agregarFavorito(Favorito favorito);

	public abstract int eliminarFavorito(Favorito favorito);

	public abstract Favorito editarFavorito(Favorito favorito);

}
