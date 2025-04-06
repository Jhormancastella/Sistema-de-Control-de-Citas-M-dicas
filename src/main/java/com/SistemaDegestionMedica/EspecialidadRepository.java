package com.SistemaDegestionMedica;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.domain.entities.Especialidad;

public interface EspecialidadRepository {
    Especialidad save(Especialidad especialidad) throws SQLException;
    Optional<com.SistemaDegestionMedica.domain.entities.Especialidad> findById(int id) throws SQLException;
    List<com.SistemaDegestionMedica.domain.entities.Especialidad> findAll() throws SQLException;
    void update(Especialidad especialidad) throws SQLException;
    void delete(int id) throws SQLException;
}