package com.ts.msfavoritos.repositorio;

import com.ts.msfavoritos.repositorio.entidad.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("categoriaJpaRepository")
public interface CategoriaJpaRepository extends JpaRepository<Categoria, Serializable> {
	public abstract Categoria findById(int id);
}
