package com.SistemaDegestionMedica.application.usecase;

import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.PacienteRepository;
import com.SistemaDegestionMedica.domain.entities.Paciente;

public class PacienteService {

    public PacienteService(PacienteRepository pacienteRepo) {
        //TODO Auto-generated constructor stub
    }

    public List<Paciente> listarPacientes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarPacientes'");
    }

    public Optional<Paciente> obtenerPaciente(int pacienteId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerPaciente'");
    }

    public void eliminarPaciente(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarPaciente'");
    }

    public void actualizarPaciente(Paciente paciente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarPaciente'");
    }

    public Paciente crearPaciente(Paciente paciente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearPaciente'");
    }

}
