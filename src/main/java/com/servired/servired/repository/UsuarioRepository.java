package com.servired.servired.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.servired.servired.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);
}