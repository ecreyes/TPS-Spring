package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Favorito;

import java.util.List;
/**Interfaz para realizar la comunicación
 * con rabbit y el controlador de Favoritos**/
public interface FavoritoMsg {

  void enviarMsg(Favorito favorito, String routeKey);

  List<Favorito> obtenerListaFavoritosUsuario(String id_usuario);
}
