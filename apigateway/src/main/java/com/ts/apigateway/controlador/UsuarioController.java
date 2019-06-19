package com.ts.apigateway.controlador;

import com.ts.apigateway.mensajeria.UsuarioMsgAdapter;
import com.ts.apigateway.mensajeria.UsuarioMsgAdapterImpl;
import com.ts.apigateway.modelo.Usuario;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UsuarioController {

    private final UsuarioMsgAdapter usuarioMsgAdapter;
    private static final Log LOGGER = LogFactory.getLog(UsuarioMsgAdapterImpl.class);

    public UsuarioController(@Qualifier("usuarioMsgAdapter") UsuarioMsgAdapter usuarioMsgAdapter) {
        this.usuarioMsgAdapter = usuarioMsgAdapter;
    }

    @PostMapping("/usuarios/agregar")
    public Usuario add(@RequestBody Usuario usuario){
        usuarioMsgAdapter.send(usuario);
        return usuario;
    }

    @PostMapping("/usuarios/login")
    public void login(@RequestBody Usuario usuario) {

        Map<String,Object > map = usuarioMsgAdapter.requestLogin(usuario);
        LOGGER.info("En UsuarioController -> status: "+map.get("STATUS")+", Usuario: "+map.get("Usuario"));
    }
}
