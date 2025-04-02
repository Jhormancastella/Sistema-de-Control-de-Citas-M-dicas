package com.SistemaDegestionMedica;

import com.SistemaDegestionMedica.domain.entities.Medico;
import java.util.List;
import java.util.Optional;

public interface MedicoRepository {
    Medico save(Medico medico);
    Optional<Medico> findById(int id);
    List<Medico> findAll();
    void update(Medico medico);
    void delete(int id);
    
    // Additional useful methods
    List<Medico> findByEspecialidad(int especialidadId);
    List<Medico> findByNombre(String nombre);
}