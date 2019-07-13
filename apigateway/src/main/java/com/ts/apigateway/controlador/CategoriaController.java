package com.ts.apigateway.controlador;


import com.ts.apigateway.mensajeria.CategoriaMsgAdapter;
import com.ts.apigateway.modelo.Categoria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class CategoriaController {

	private static final String MENU_CATEGORIAS = "categorias";
	private static final String FORM_CATEGORIAS = "categorias_form";

	private static final Log LOGGER = LogFactory.getLog(CategoriaController.class);

	private final CategoriaMsgAdapter categoriaMsgAdapter;

	public CategoriaController(@Qualifier("categoriaMsgAdapter") CategoriaMsgAdapter categoriaMsgAdapter) {
		this.categoriaMsgAdapter = categoriaMsgAdapter;
	}

	@GetMapping("/categoria")
	public List<Categoria> index() {
		return categoriaMsgAdapter.getList();
	}

	/*@GetMapping("/categorias/agregar")
	public String show_form(Model model) {
		model.addAttribute("categoria", new Categoria());
		return FORM_CATEGORIAS;
	}*/

	@PostMapping("/categoria/agregar")
	public Categoria add(@RequestBody Categoria categoria) {
		/*ModelAndView model = new ModelAndView(MENU_CATEGORIAS);
		model.addObject("categorias", categoria);*/

		LOGGER.info("Recibido desde post: " + categoria.toString());

		categoriaMsgAdapter.send(categoria);
		return categoria;
	}
}
