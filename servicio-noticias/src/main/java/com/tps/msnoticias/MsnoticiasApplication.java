package com.tps.msnoticias;

import com.tps.msnoticias.mensajeria.MsgAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsnoticiasApplication implements CommandLineRunner {
	private final MsgAdapter msgAdapter;

	public MsnoticiasApplication(@Qualifier("MsgAdapter") MsgAdapter msgAdapter) {
		this.msgAdapter = msgAdapter;
	}

	public static void main(String[] args) {
		SpringApplication.run(MsnoticiasApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		msgAdapter.processCreate();
	}

}
