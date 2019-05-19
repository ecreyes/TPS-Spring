package com.ts.apigateway.controlador;

import com.ts.apigateway.mensajeria.NoticiaAdapter;
import com.ts.apigateway.modelo.Noticia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticiasController {
    @Autowired
    @Qualifier("NoticiaAdapterImpl")
    private NoticiaAdapter noticiaAdapter;

    @PostMapping("/addnoticia")
    public Noticia addNoticia(@RequestBody Noticia noticia){
        noticiaAdapter.send(noticia);
        return noticia;
    }
}
