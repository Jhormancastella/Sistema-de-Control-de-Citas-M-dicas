package com.SistemaDegestionMedica.application.usecase;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.CitaRepository;
import com.SistemaDegestionMedica.MedicoRepository;
import com.SistemaDegestionMedica.PacienteRepository;
import com.SistemaDegestionMedica.domain.entities.Cita;

public class CitaService {
    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public CitaService(CitaRepository citaRepository, 
                      PacienteRepository pacienteRepository,
                      MedicoRepository medicoRepository) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    public Optional<Cita> obtenerCita(int id) {
        return citaRepository.findById(id);
    }

    public boolean cancelarCita(int id) {
        Optional<Cita> citaOpt = citaRepository.findById(id);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            cita.setEstado("Cancelada");
            citaRepository.update(cita);
            return true;
        }
        return false;
    }

    public Cita agendarCita(Cita cita) throws IllegalArgumentException, SQLException {
        // Validar que el paciente existe
        pacienteRepository.findById(cita.getPacienteId())
            .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con ID: " + cita.getPacienteId()));
        
        // Validar que el médico existe
        medicoRepository.findById(cita.getMedicoId())
            .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado con ID: " + cita.getMedicoId()));
        
        // Validar disponibilidad del médico
        if (!citaRepository.isMedicoDisponible(cita.getMedicoId(), cita.getFechaHora())) {
            throw new IllegalStateException("El médico no está disponible en ese horario");
        }
        
        // Establecer estado por defecto si no viene especificado
        if (cita.getEstado() == null || cita.getEstado().isEmpty()) {
            cita.setEstado("Programada");
        }
        
        return citaRepository.save(cita);
    }

    public Cita reprogramarCita(int id, LocalDateTime nuevaFechaHora) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));
        
        // Validar que la nueva fecha no sea en el pasado
        if (nuevaFechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede programar una cita en el pasado");
        }
        
        // Validar disponibilidad del médico en el nuevo horario
        if (!citaRepository.isMedicoDisponible(cita.getMedicoId(), nuevaFechaHora)) {
            throw new IllegalStateException("El médico no está disponible en el nuevo horario");
        }
        
        cita.setFechaHora(nuevaFechaHora);
        cita.setEstado("Reprogramada");
        
        return citaRepository.update(cita);
    }

    public List<Cita> listarCitasPorPaciente(int pacienteId) throws IllegalArgumentException, SQLException {
        // Validar que el paciente existe
        pacienteRepository.findById(pacienteId)
            .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con ID: " + pacienteId));
        
        return citaRepository.findByPacienteId(pacienteId);
    }

    public List<Cita> listarCitasPorMedico(int medicoId) {
        // Validar que el médico existe
        medicoRepository.findById(medicoId)
            .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado con ID: " + medicoId));
        
        return citaRepository.findByMedicoId(medicoId);
    }

    public List<Cita> listarCitasPorEstado(String estado) {
        // Validar que el estado sea válido
        if (!List.of("Programada", "Confirmada", "Cancelada", "Completada").contains(estado)) {
            throw new IllegalArgumentException("Estado de cita no válido: " + estado);
        }
        
        return citaRepository.findAll().stream()
            .filter(c -> c.getEstado().equalsIgnoreCase(estado))
            .toList();
    }
}