package com.ts.mscategoria;

import com.ts.mscategoria.mensajeria.MsgAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MscategoriaApplication implements CommandLineRunner {

    private final MsgAdapter msgAdapter;

    public MscategoriaApplication(@Qualifier("mensajero") MsgAdapter msgAdapter) {
        this.msgAdapter = msgAdapter;
    }

    public static void main(String[] args) {
        SpringApplication.run(MscategoriaApplication.class, args);
    }


    @Override
    public void run(String... args) {

        switch (args[0]) {
            case "All":
                msgAdapter.processCreate();
                msgAdapter.processList();
                break;
            case "Creacion":
                msgAdapter.processCreate();
                break;
            case "Lista":
                msgAdapter.processList();
                break;
        }
    }
}
