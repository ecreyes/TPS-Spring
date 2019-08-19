package com.ts.msfavoritos.servicio;

import com.ts.msfavoritos.dominio.FavoritoRoot;
import java.util.List;

/**Servicio de favorito con los metodos de agregar,eliminar u obtener un listado de favoritos*/
public interface FavoritoService {

  void agregar(FavoritoRoot favorito);

  void eliminar(FavoritoRoot favorito);

  List<FavoritoRoot> getListaFavUsuario(int id_usuario);
}
