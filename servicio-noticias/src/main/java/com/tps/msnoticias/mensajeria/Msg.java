package com.tps.msnoticias.mensajeria;

/**interfaz para realizar los metodos de la comunicación con rabbit**/
public interface Msg {

  void procesarCUD();

  void procesarListadoNoticias();

  void procesarListadoCategorias();
}
