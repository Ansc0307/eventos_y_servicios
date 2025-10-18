package com.eventos.ms_usuarios.repository;

import com.eventos.ms_usuarios.model.UsuarioLoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioLoginLogRepository extends JpaRepository<UsuarioLoginLog, Long> {
  Page<UsuarioLoginLog> findByEmailIgnoreCaseOrderByCreadoEnDesc(String email, Pageable pageable);

  Page<UsuarioLoginLog> findByUsuario_IdOrderByCreadoEnDesc(Long usuarioId, Pageable pageable);
}
