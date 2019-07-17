package com.ts.apigateway.controlador;

import com.ts.apigateway.mensajeria.UsuarioMsg;
import com.ts.apigateway.mensajeria.UsuarioMsgImpl;
import com.ts.apigateway.modelo.Usuario;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UsuarioController {

    private final UsuarioMsg usuarioMsg;
    private static final String ROUTE_KEY_CREATE = "usuario.crear";
    private static final String ROUTE_KEY_DELETE = "usuario.eliminar";
    private static final String ROUTE_KEY_EDIT = "usuario.editar";

    public UsuarioController(@Qualifier("usuarioMsgAdapter") UsuarioMsg usuarioMsg) {
        this.usuarioMsg = usuarioMsg;
    }

    @PostMapping("/usuario/agregar")
    public void agregar(@RequestBody Usuario usuario) {
        usuarioMsg.send(usuario, ROUTE_KEY_CREATE);
    }

    @PutMapping("/usuario/editar")
    public void editar(@RequestBody Usuario usuario) {
        usuarioMsg.send(usuario, ROUTE_KEY_EDIT);
    }

    @DeleteMapping("/usuario/eliminar")
    public void eliminar(@RequestBody Usuario usuario) {
        usuarioMsg.send(usuario, ROUTE_KEY_DELETE);
    }

    @PostMapping("/usuario/login")
    public String login(@RequestBody Usuario usuario) {
        return usuarioMsg.requestLogin(usuario);
    }
}
