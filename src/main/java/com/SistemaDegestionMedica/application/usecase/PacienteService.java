package com.SistemaDegestionMedica.application.usecase;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.PacienteRepository;
import com.SistemaDegestionMedica.domain.entities.Paciente;

public class PacienteService {
    private final PacienteRepository pacienteRepo;

    public PacienteService(PacienteRepository pacienteRepo) {
        this.pacienteRepo = pacienteRepo;
    }

    public Paciente crearPaciente(Paciente paciente) {
        try {
            return pacienteRepo.save(paciente);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear paciente", e);
        }
    }

    public Optional<Paciente> obtenerPaciente(int id) {
        try {
            return pacienteRepo.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener paciente con ID: " + id, e);
        }
    }

    public void actualizarPaciente(Paciente paciente) {
        try {
            pacienteRepo.update(paciente);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar paciente", e);
        }
    }

    public List<Paciente> listarPacientes() {
        try {
            return pacienteRepo.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pacientes", e);
        }
    }

    public void eliminarPaciente(int id) {
        try {
            pacienteRepo.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar paciente con ID: " + id, e);
        }
    }

    public void registrarPaciente(Paciente nuevoPaciente) {
        this.crearPaciente(nuevoPaciente);
    }
}