package com.ts.apigateway.controlador;

import com.ts.apigateway.mensajeria.NoticiaMsg;
import com.ts.apigateway.modelo.Noticia;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoticiasController {

    private static final String ROUTE_KEY_CREATE = "noticia.crear";
    private static final String ROUTE_KEY_EDIT = "noticia.editar";
    private static final String ROUTE_KEY_DELETE = "noticia.eliminar";

    private static final Log LOGGER = LogFactory.getLog(NoticiasController.class);

    private final NoticiaMsg noticiaMsg;

    public NoticiasController(@Qualifier("noticiaMsgAdapter") NoticiaMsg noticiaMsg) {
        this.noticiaMsg = noticiaMsg;
    }

    @GetMapping("/noticias")
    public List<Noticia> noticias() {
        return noticiaMsg.getList();
    }

    @PostMapping("/noticia/agregar")
    public Noticia agregar(@RequestBody Noticia noticia) {
        noticiaMsg.send(noticia, ROUTE_KEY_CREATE);
        return noticia;
    }

    @DeleteMapping("/noticia/eliminar")
    public Noticia eliminar(@RequestBody Noticia noticia) {
        noticiaMsg.send(noticia, ROUTE_KEY_DELETE);
        return noticia;
    }

    @PutMapping("/noticia/editar")
    public Noticia editar(@RequestBody Noticia noticia) {
        noticiaMsg.send(noticia, ROUTE_KEY_EDIT);
        return noticia;
    }
}
