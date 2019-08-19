package com.ts.mscategoria.mensajeria;

/**Interfaz para declarar los métodos que se utilzaran para la conexion con rabbit**/
public interface Msg {

  void procesarCUD();

  void procesarListaCategorias();
}
