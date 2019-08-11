package com.ts.mscategoria;

import com.ts.mscategoria.mensajeria.Msg;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MscategoriaApplication implements CommandLineRunner {

  private final Msg msg;

  public MscategoriaApplication(@Qualifier("msgAdapter") Msg msg) {
    this.msg = msg;
  }

  public static void main(String[] args) {
    SpringApplication.run(MscategoriaApplication.class, args);
  }


  @Override
  public void run(String... args) {

    msg.procesarCUD();
    msg.procesarListaCategorias();
  }
}
