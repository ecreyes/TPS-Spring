/*package com.ts.mscategoria.controlador;


import com.ts.mscategoria.repositorio.entidad.Categoria;
import com.ts.mscategoria.servicio.CategoriaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

	private final CategoriaService categoriaService;

	public CategoriaController(@Qualifier("categoriaService") CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}

	@PostMapping("/crear")
	public Categoria agregarCategoria(@RequestBody Categoria categoria) {
		return categoriaService.agregarCategoria(categoria);
	}
}*/
