package com.ts.apigateway.controlador;


import com.ts.apigateway.mensajeria.CategoriaMsgAdapter;
import com.ts.apigateway.modelo.Categoria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/menu")
public class CategoriaController {

	private static final String MENU_CATEGORIAS = "categorias";
	private static final String FORM_CATEGORIAS = "categorias_form";

	private static final Log LOGGER = LogFactory.getLog(CategoriaController.class);

	private final CategoriaMsgAdapter categoriaMsgAdapter;

	public CategoriaController(@Qualifier("categoriaMsgAdapter") CategoriaMsgAdapter categoriaMsgAdapter) {
		this.categoriaMsgAdapter = categoriaMsgAdapter;
	}

	@GetMapping("/categorias")
	public ModelAndView index() {

		ModelAndView modelAndView = new ModelAndView(MENU_CATEGORIAS);
		modelAndView.addObject("categorias",categoriaMsgAdapter.getList());
		return modelAndView;
	}

	@GetMapping("/categorias/agregar")
	public String show_form(Model model) {
		model.addAttribute("categoria", new Categoria());
		return FORM_CATEGORIAS;
	}

	@PostMapping("/categorias/agregar")
	public String add(@ModelAttribute("categoria") Categoria categoria) {
		ModelAndView model = new ModelAndView(MENU_CATEGORIAS);
		model.addObject("categorias", categoria);

		LOGGER.info("Recibido desde post: " + categoria.toString());

		categoriaMsgAdapter.send(categoria);
		return "redirect:/menu/categorias";
	}
}
