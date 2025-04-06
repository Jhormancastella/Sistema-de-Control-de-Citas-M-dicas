package com.SistemaDegestionMedica;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.domain.entities.Cita;

public interface CitaRepository {
    Cita save(Cita cita);
    Optional<Cita> findById(int id);
    List<Cita> findAll();
    Cita update(Cita cita);
    void delete(int id);
    boolean isMedicoDisponible(int medicoId, LocalDateTime fechaHora);
    List<Cita> findByPacienteId(int pacienteId);
    List<Cita> findByMedicoId(int medicoId);
}