package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Noticia;

public interface NoticiaAdapter {
    void send(Noticia noticia);
}
