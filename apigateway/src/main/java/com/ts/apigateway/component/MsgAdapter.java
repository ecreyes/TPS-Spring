package com.ts.apigateway.component;

import com.ts.apigateway.model.Categoria;

public interface MsgAdapter {

	void send(Categoria categoria);

	//Debe ser implementado en el servicio categoria
	void receive();
}
