package com.SistemaDegestionMedica;

import java.util.List;
import java.util.Optional;
import com.SistemaDegestionMedica.domain.entities.Especialidad;

public interface EspecialidadRepository {
    Especialidad save(Especialidad especialidad);
    Optional<Especialidad> findById(int id);
    List<Especialidad> findAll();
    void update(Especialidad especialidad);
    void delete(int id);
}