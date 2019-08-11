package com.tps.msusuario.repositorio;

import com.tps.msusuario.repositorio.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("usuarioJpaRespository")
public interface UsuarioJpaRepository extends JpaRepository<Usuario, Serializable> {

  //Busqueda personalizada por mail
  Usuario findUsuarioByEmail(String email);
}
