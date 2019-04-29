package com.ts.msfavoritos.repositorio;

import com.ts.msfavoritos.repositorio.entidad.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface FavoritoJpaRepository extends JpaRepository<Favorito, Serializable> {
	public abstract Favorito findById(int id);
}
