package com.servired.servired.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.servired.servired.model.Profesional;
import com.servired.servired.model.Usuario;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {

    List<Profesional> findByEstado(String estado);

    Profesional findByUsuario(Usuario usuario);

    List<Profesional> findByEstadoAndNombreContainingIgnoreCase(
            String estado, String nombre);

    List<Profesional> findByEstadoAndProfesionContainingIgnoreCase(
            String estado, String profesion);
}