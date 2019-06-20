package com.ts.apigateway.controlador;

import com.ts.apigateway.mensajeria.NoticiaMsgAdapter;
import com.ts.apigateway.modelo.Noticia;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticiasController {

    private final NoticiaMsgAdapter noticiaMsgAdapter;

    public NoticiasController(@Qualifier("noticiaMsgAdapter") NoticiaMsgAdapter noticiaMsgAdapter) {
        this.noticiaMsgAdapter = noticiaMsgAdapter;
    }

    @PostMapping("/addnoticia")
    public Noticia addNoticia(@RequestBody Noticia noticia) {
        noticiaMsgAdapter.send(noticia);
        return noticia;
    }
}
