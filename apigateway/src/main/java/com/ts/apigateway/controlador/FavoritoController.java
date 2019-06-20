package com.ts.apigateway.controlador;

import com.ts.apigateway.mensajeria.FavoritoMsgAdapter;
import com.ts.apigateway.modelo.Favorito;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoritoController {

    private final FavoritoMsgAdapter favoritoMsgAdapter;
    private static final Log LOGGER = LogFactory.getLog(FavoritoController.class);

    public FavoritoController(@Qualifier("favoritoMsgAdapter") FavoritoMsgAdapter favoritoMsgAdapter) {
        this.favoritoMsgAdapter = favoritoMsgAdapter;
    }

    @PostMapping("/favorito/agregar")
    public void add(@RequestBody Favorito favorito) {
        favoritoMsgAdapter.send(favorito);
    }

}
