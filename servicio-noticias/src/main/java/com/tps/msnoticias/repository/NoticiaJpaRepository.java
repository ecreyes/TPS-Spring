package com.tps.msnoticias.repository;

import com.tps.msnoticias.repository.entity.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("noticiaJpaRepository")
public interface NoticiaJpaRepository extends JpaRepository<Noticia, Serializable> {
}
