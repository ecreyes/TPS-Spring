package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Categoria;

public interface MsgAdapter {

	void send(Categoria categoria);

	//Debe ser implementado en el servicio categoria
	void receive();
}
