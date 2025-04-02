package com.SistemaDegestionMedica.application.usecase;

import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.MedicoRepository;
import com.SistemaDegestionMedica.domain.entities.Medico;

public class MedicoService {
    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    public Optional<Medico> obtenerMedico(int medicoId) {
        return medicoRepository.findById(medicoId);
    }

    public void actualizarMedico(Medico medico) {
        medicoRepository.update(medico);
    }

    public Medico crearMedico(Medico medico) {
        return medicoRepository.save(medico);
    }

    public void eliminarMedico(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarMedico'");
    }
}