package com.tps.msusuario;

import com.tps.msusuario.mensajeria.Msg;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsusuarioApplication implements CommandLineRunner {

  private final Msg msg;

  public MsusuarioApplication(@Qualifier("msgAdapter") Msg msg) {
    this.msg = msg;
  }

  public static void main(String[] args) {
    SpringApplication.run(MsusuarioApplication.class, args);
  }

  @Override
  public void run(String... args) {

    msg.procesarCUD();
    msg.procesarLogin();
  }
}
