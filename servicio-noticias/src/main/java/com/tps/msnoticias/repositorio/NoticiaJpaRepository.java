package com.tps.msnoticias.repositorio;

import com.tps.msnoticias.repositorio.entidad.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("noticiaJpaRepository")
public interface NoticiaJpaRepository extends JpaRepository<Noticia, Serializable> {

}
