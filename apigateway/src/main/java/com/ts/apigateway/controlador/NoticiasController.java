package com.ts.apigateway.controlador;

import com.ts.apigateway.mensajeria.NoticiaAdapter;
import com.ts.apigateway.modelo.Noticia;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticiasController {

    private final NoticiaAdapter noticiaAdapter;

    public NoticiasController(@Qualifier("NoticiaAdapter") NoticiaAdapter noticiaAdapter) {
        this.noticiaAdapter = noticiaAdapter;
    }

    @PostMapping("/addnoticia")
    public Noticia addNoticia(@RequestBody Noticia noticia) {
        noticiaAdapter.send(noticia);
        return noticia;
    }
}
