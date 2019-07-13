package com.ts.apigateway.controlador;


import com.ts.apigateway.mensajeria.CategoriaMsg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoriaController {

    private static final String ROUTE_KEY_CREATE = "categoria.crear";
    private static final String ROUTE_KEY_EDIT = "categoria.editar";

    private static final Log LOGGER = LogFactory.getLog(CategoriaController.class);

    private final CategoriaMsg categoriaMsgMsgAdapter;

    public CategoriaController(@Qualifier("categoriaMsgAdapter") CategoriaMsg categoriaMsgMsgAdapter) {
        this.categoriaMsgMsgAdapter = categoriaMsgMsgAdapter;
    }

    @GetMapping("/categoria")
    public List<com.ts.apigateway.modelo.Categoria> index() {
        return categoriaMsgMsgAdapter.getList();
    }

    @PostMapping("/categoria/agregar")
    public com.ts.apigateway.modelo.Categoria agregar(@RequestBody com.ts.apigateway.modelo.Categoria categoria) {

        LOGGER.info("Recibido desde post: " + categoria.toString());

        categoriaMsgMsgAdapter.send(categoria, ROUTE_KEY_CREATE);
        return categoria;
    }

    @PutMapping("/categoria/editar")
    public com.ts.apigateway.modelo.Categoria editar(@RequestBody com.ts.apigateway.modelo.Categoria categoria) {

        LOGGER.info("Recibido desde post: " + categoria.toString());

        categoriaMsgMsgAdapter.send(categoria, ROUTE_KEY_EDIT);

        return categoria;
    }
}
