package com.SistemaDegestionMedica;

import com.SistemaDegestionMedica.domain.entities.Paciente;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository {
    Paciente save(Paciente paciente);
    Optional<Paciente> findById(float id);
    List<Paciente> findAll();
    void update(Paciente paciente);
    void delete(int id);
    Optional<Paciente> findByDocumento(String documento);
}