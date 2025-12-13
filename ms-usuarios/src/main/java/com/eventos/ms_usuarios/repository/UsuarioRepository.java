package com.eventos.ms_usuarios.repository;

import com.eventos.ms_usuarios.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  // Derived query: buscar por email
  Optional<Usuario> findByEmail(String email);

  // Derived query: buscar por Keycloak subject (sub)
  Optional<Usuario> findByKeycloakId(String keycloakId);

  // Derived query: verificar existencia por email
  boolean existsByEmail(String email);

  // Derived query: listar solo activos, paginado
  Page<Usuario> findByActivoTrue(Pageable pageable);

  // Native query: buscar por dominio de email (ej: gmail.com), paginado
  @Query(value = "select * from usuarios where lower(email) like concat('%@', lower(:dominio))", countQuery = "select count(*) from usuarios where lower(email) like concat('%@', lower(:dominio))", nativeQuery = true)
  Page<Usuario> buscarPorDominioEmail(@Param("dominio") String dominio, Pageable pageable);
}
