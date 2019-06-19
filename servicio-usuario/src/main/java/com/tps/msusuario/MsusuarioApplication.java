package com.tps.msusuario;

import com.tps.msusuario.mensajeria.MsgAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsusuarioApplication implements CommandLineRunner {

	private final MsgAdapter msgAdapter;

	public MsusuarioApplication(@Qualifier("MsgAdapter") MsgAdapter msgAdapter) {
		this.msgAdapter = msgAdapter;
	}

	public static void main(String[] args) {
		SpringApplication.run(MsusuarioApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//msgAdapter.processCreate();
		msgAdapter.processLogin();
	}
}
