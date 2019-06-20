package com.ts.msfavoritos.repositorio;

import com.ts.msfavoritos.repositorio.entidad.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("favoritoJpaRepository")
public interface FavoritoJpaRepository extends JpaRepository<Favorito, Serializable> {

}
