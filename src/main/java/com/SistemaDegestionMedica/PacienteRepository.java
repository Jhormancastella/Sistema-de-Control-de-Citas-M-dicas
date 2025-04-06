package com.SistemaDegestionMedica;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.domain.entities.Paciente;

public interface PacienteRepository {
    Paciente save(Paciente paciente) throws SQLException;
    Optional<Paciente> findById(int id) throws SQLException;
    List<Paciente> findAll() throws SQLException;
    void update(Paciente paciente) throws SQLException;
    void delete(int id) throws SQLException;
}