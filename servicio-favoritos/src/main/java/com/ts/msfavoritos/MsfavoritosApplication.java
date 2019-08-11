package com.ts.msfavoritos;

import com.ts.msfavoritos.mensajeria.Msg;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsfavoritosApplication implements CommandLineRunner {

  private final Msg msg;

  public MsfavoritosApplication(@Qualifier("msgAdapter") Msg msg) {
    this.msg = msg;
  }

  public static void main(String[] args) {
    SpringApplication.run(MsfavoritosApplication.class, args);
  }

  @Override
  public void run(String... args) {

    msg.procesarCD();
    msg.procesarListaFavUsuario();
  }
}
