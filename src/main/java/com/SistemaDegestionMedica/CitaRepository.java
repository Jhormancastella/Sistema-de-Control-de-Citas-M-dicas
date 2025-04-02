package com.SistemaDegestionMedica;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.adapter.ui.CitaMenu;
import com.SistemaDegestionMedica.domain.entities.Cita;

public interface CitaRepository {
    Cita save(Cita cita);
    Optional<CitaMenu> findById(int id);
    List<Cita> findAll();
    Cita update(Cita cita);
    void delete(int id);
    boolean isMedicoDisponible(int medicoId, String nuevaFechaHora);
    List<Cita> findByPacienteId(int pacienteId);
    List<Cita> findByMedicoId(int medicoId);
    boolean isMedicoDisponible(int medicoId, LocalDateTime nuevaFechaHora);
    boolean isMedicoDisponible(String medicoId, LocalDateTime nuevaFechaHora);
    Cita update(CitaMenu cita);
}