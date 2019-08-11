package com.tps.msnoticias;

import com.tps.msnoticias.mensajeria.Msg;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsnoticiasApplication implements CommandLineRunner {

  private final Msg msg;

  public MsnoticiasApplication(@Qualifier("msgAdapter") Msg msg) {
    this.msg = msg;
  }

  public static void main(String[] args) {
    SpringApplication.run(MsnoticiasApplication.class, args);
  }

  @Override
  public void run(String... args) {

    msg.procesarCUD();
    msg.procesarListadoCategorias();
    msg.procesarListadoNoticias();

  }
}
