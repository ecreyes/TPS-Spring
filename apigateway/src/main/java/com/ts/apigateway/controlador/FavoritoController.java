package com.ts.apigateway.controlador;

import com.ts.apigateway.mensajeria.FavoritoMsg;
import com.ts.apigateway.modelo.Favorito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FavoritoController {

  private final FavoritoMsg favoritoMsg;

  private static final String ROUTE_KEY_CREATE = "favorito.crear";
  private static final String ROUTE_KEY_DELETE = "favorito.eliminar";

  public FavoritoController(@Qualifier("favoritoMsgAdapter") FavoritoMsg favoritoMsg) {
    this.favoritoMsg = favoritoMsg;
  }

  @PostMapping("/favorito/agregar")
  public void agregar(@RequestBody Favorito favorito) {
    favoritoMsg.enviarMsg(favorito, ROUTE_KEY_CREATE);
  }

  @DeleteMapping("/favorito/eliminar")
  public void eliminar(@RequestBody Favorito favorito) {
    favoritoMsg.enviarMsg(favorito, ROUTE_KEY_DELETE);
  }

  @GetMapping("/favorito/usuario/{id}")
  public List<Favorito> favoritosUsuario(@PathVariable(name = "id") String id_usuario) {
    return favoritoMsg.obtenerListaFavoritosUsuario(id_usuario);
  }


}
