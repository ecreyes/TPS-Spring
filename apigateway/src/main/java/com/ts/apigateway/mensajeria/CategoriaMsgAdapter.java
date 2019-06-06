package com.ts.apigateway.mensajeria;

import com.ts.apigateway.modelo.Categoria;

import java.util.ArrayList;
import java.util.List;

public interface CategoriaMsgAdapter {

	void send(Categoria categoria);

	List<Categoria> getList();
}
