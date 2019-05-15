package com.ts.apigateway.controlador;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/menu")
public class ApiGatewayController {

	private static final String MENU_CATEGORIAS = "categorias";
	private static final String FORM_CATEGORIAS = "categorias_form";

	private static final Log LOGGER = LogFactory.getLog(ApiGatewayController.class);

	@GetMapping("/categorias")
	public String index() {
		return MENU_CATEGORIAS;
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

		LOGGER.info("Envio post: " + categoria.toString());
		return "redirect:/menu/categorias";
	}
}
