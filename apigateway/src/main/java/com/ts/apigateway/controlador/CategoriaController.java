package com.ts.apigateway.controlador;


import com.ts.apigateway.mensajeria.CategoriaMsg;
import com.ts.apigateway.modelo.Categoria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoriaController {

    private static final String ROUTE_KEY_CREATE = "categoria.crear";
    private static final String ROUTE_KEY_EDIT = "categoria.editar";
    private static final String ROUTE_KEY_DELETE = "categoria.eliminar";

    private static final Log LOGGER = LogFactory.getLog(CategoriaController.class);

    private final CategoriaMsg categoriaMsgMsgAdapter;

    public CategoriaController(@Qualifier("categoriaMsgAdapter") CategoriaMsg categoriaMsgMsgAdapter) {
        this.categoriaMsgMsgAdapter = categoriaMsgMsgAdapter;
    }

    @GetMapping("/categorias")
    public List<Categoria> categorias() {
        return categoriaMsgMsgAdapter.getList();
    }

    @PostMapping("/categoria/agregar")
    public Categoria agregar(@RequestBody Categoria categoria) {

        LOGGER.info("Recibido desde post: " + categoria.toString());

        categoriaMsgMsgAdapter.send(categoria, ROUTE_KEY_CREATE);
        return categoria;
    }

    @PutMapping("/categoria/editar")
    public Categoria editar(@RequestBody Categoria categoria) {

        LOGGER.info("Recibido desde put: " + categoria.toString());

        categoriaMsgMsgAdapter.send(categoria, ROUTE_KEY_EDIT);

        return categoria;
    }

    @DeleteMapping("/categoria/eliminar")
    public Categoria eliminar(@RequestBody Categoria categoria) {
        LOGGER.info("Recibido desde delete: " + categoria.toString());

        categoriaMsgMsgAdapter.send(categoria, ROUTE_KEY_DELETE);

        return categoria;
    }
}
