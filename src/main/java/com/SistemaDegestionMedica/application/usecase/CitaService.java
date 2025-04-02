package com.SistemaDegestionMedica.application.usecase;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.CitaRepository;
import com.SistemaDegestionMedica.MedicoRepository;
import com.SistemaDegestionMedica.PacienteRepository;
import com.SistemaDegestionMedica.adapter.ui.CitaMenu;
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
        citaRepository.delete(id);
    }

    public Cita agendarCita(Cita cita) {
        // Validar que el paciente existe
        pacienteRepository.findById(cita.getPacienteId())
            .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        
        // Validar que el médico existe
        medicoRepository.findById(cita.getMedicoId())
            .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));
        
        // Validar disponibilidad del médico
        if (!citaRepository.isMedicoDisponible(cita.getMedicoId(), cita.getFechaHora())) {
            throw new IllegalStateException("El médico no está disponible en ese horario");
        }
        
        return citaRepository.save(cita);
    }

    public Cita reprogramarCita(int id, LocalDateTime nuevaFechaHora) {
        CitaMenu cita = citaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        
        // Validar disponibilidad del médico
        if (!citaRepository.isMedicoDisponible(cita.getMedicoId(), nuevaFechaHora)) {
            throw new IllegalStateException("El médico no está disponible en el nuevo horario");
        }
        
        cita.setFechaHora(nuevaFechaHora);
        return citaRepository.update(cita);
    }

    public List<Cita> listarCitasPorPaciente(int pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }

    public List<Cita> listarCitasPorMedico(int medicoId) {
        return citaRepository.findByMedicoId(medicoId);
    }

    public Cita reprogramarCita(int id, Date nuevaFechaHora) {
        LocalDateTime localDateTime = new Date(nuevaFechaHora.getTime())
            .toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime();
        return reprogramarCita(id, localDateTime);
    }
}