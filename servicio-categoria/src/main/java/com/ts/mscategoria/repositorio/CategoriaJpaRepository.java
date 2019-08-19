package com.ts.mscategoria.repositorio;

import com.ts.mscategoria.repositorio.entidad.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**JPA para categoria**/
@Repository("categoriaJpaRepository")
public interface CategoriaJpaRepository extends JpaRepository<Categoria, Serializable> {

}
