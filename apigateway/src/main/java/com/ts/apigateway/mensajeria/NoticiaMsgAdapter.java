package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Noticia;

public interface NoticiaMsgAdapter {
    void send(Noticia noticia);
}
