package com.ts.msfavoritos;

import com.ts.msfavoritos.mensajeria.MsgAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsfavoritosApplication implements CommandLineRunner {

	private final MsgAdapter msgAdapter;

	public MsfavoritosApplication(@Qualifier("msgAdapter") MsgAdapter msgAdapter) {
		this.msgAdapter = msgAdapter;
	}

	public static void main(String[] args) {
		SpringApplication.run(MsfavoritosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		msgAdapter.processFavorite();
	}
}
