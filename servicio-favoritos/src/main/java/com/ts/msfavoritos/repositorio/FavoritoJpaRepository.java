package com.ts.msfavoritos.repositorio;

import com.ts.msfavoritos.repositorio.entidad.Favorito;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**JPA de favorito**/
@Repository("favoritoJpaRepository")
public interface FavoritoJpaRepository extends JpaRepository<Favorito, Serializable> {

  @Query("select t from #{#entityName} t where t.idUsuario = ?1")
  List<Favorito> findAllByIdUsuario(int idUsuario);

  //Metodo JPA personalizado para buscar por idUsuario e idNoticia en vez de usar PK id_favorito
  @Query("select t from #{#entityName} t where t.idUsuario = ?1 and t.idNoticia = ?2")
  Optional<Favorito> findByIdUsuarioAndIdNoticia(int idUsuario, int idNoticia);

  //Metodo JPA personalizado para eliminacion por idUsuario e idNoticia en vez de usar PK id_favorito
  @Transactional
  @Modifying
  @Query("delete from #{#entityName} u where u.idUsuario = ?1 and u.idNoticia = ?2")
  void deleteByIdUsuarioAndIdNoticia(int idUsuario, int idNoticia);
}
