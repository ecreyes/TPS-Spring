package com.ts.msfavoritos.repositorio;

import com.ts.msfavoritos.repositorio.entidad.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

@Repository("favoritoJpaRepository")
public interface FavoritoJpaRepository extends JpaRepository<Favorito, Serializable> {

    //Metodo JPA personalizado para buscar por id_usuario e id_noticia en vez de usar PK id_favorito
    @Query("select t from #{#entityName} t where t.id_usuario = ?1 and t.id_noticia = ?2")
    Optional<Favorito> findById_usuarioAndId_noticia(int id_usuario, int id_noticia);

    //Metodo JPA personalizado para eliminacion por id_usuario e id_noticia en vez de usar PK id_favorito
    @Transactional
    @Modifying
    @Query("delete from #{#entityName} u where u.id_usuario = ?1 and u.id_noticia = ?2")
    void deleteById_usuarioAndId_noticia(int id_usuario, int id_noticia);
}
