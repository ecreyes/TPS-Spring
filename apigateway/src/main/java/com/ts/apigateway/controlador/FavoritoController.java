package com.ts.apigateway.controlador;

import com.ts.apigateway.mensajeria.FavoritoMsg;
import com.ts.apigateway.modelo.Favorito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Clase tipo Controller encargada de la recepción
 * de las rutas para el manejo de peticiones a favorito
 * **/

@RestController
public class FavoritoController {

  private final FavoritoMsg favoritoMsg;

  /**Nombre de las colas que estan en rabbit, para enviar los mensajes ahí**/
  private static final String ROUTE_KEY_CREATE = "favorito.crear";
  private static final String ROUTE_KEY_DELETE = "favorito.eliminar";

  /**Cargar el adaptador de Favoritos**/
  public FavoritoController(@Qualifier("favoritoMsgAdapter") FavoritoMsg favoritoMsg) {
    this.favoritoMsg = favoritoMsg;
  }

  /**Método para agregar un favorito**/
  @PostMapping("/favorito/agregar")
  public void agregar(@RequestBody Favorito favorito) {
    favoritoMsg.enviarMsg(favorito, ROUTE_KEY_CREATE);
  }

  /**Método para eliminar un favorito*/
  @DeleteMapping("/favorito/eliminar")
  public void eliminar(@RequestBody Favorito favorito) {
    favoritoMsg.enviarMsg(favorito, ROUTE_KEY_DELETE);
  }

  /**Metodo para obtener un favorito*/
  @GetMapping("/favorito/usuario/{id}")
  public List<Favorito> favoritosUsuario(@PathVariable(name = "id") String id_usuario) {
    return favoritoMsg.obtenerListaFavoritosUsuario(id_usuario);
  }


}
