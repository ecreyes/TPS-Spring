package com.tps.msnoticias.repositorio;

import com.tps.msnoticias.repositorio.entidad.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**Jpa para realizar acciones en la base de datos**/
@Repository("noticiaJpaRepository")
public interface NoticiaJpaRepository extends JpaRepository<Noticia, Serializable> {

}
