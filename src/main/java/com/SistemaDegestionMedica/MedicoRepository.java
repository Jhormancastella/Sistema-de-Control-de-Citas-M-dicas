package com.SistemaDegestionMedica;

import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.domain.Medico;

public interface MedicoRepository {

    // CRUD methods
    Medico save(Medico medico);
    Optional<Medico> findById(int id);
    List<Medico> findAll();
    void update(Medico medico);
    void delete(int id);
    void deleteById(int id);

    // Additional methods
    List<Medico> findByNombre(String nombre);
    List<Medico> findByNombreAndApellido(String nombre, String apellido);
    List<Medico> findByEspecialidad(int especialidadId);
}