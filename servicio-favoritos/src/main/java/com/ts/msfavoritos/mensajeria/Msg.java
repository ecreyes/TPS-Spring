package com.ts.msfavoritos.mensajeria;

/**Interfaz con los métodos para comunicarse a las colas de rabbit**/
public interface Msg {

  void procesarCD();

  void procesarListaFavUsuario();
}
