package com.SistemaDegestionMedica.application.usecase;

import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.EspecialidadRepository;
import com.SistemaDegestionMedica.domain.entities.Especialidad;

public class EspecialidadService {
    private final EspecialidadRepository especialidadRepo;

    public EspecialidadService(EspecialidadRepository especialidadRepo) {
        this.especialidadRepo = especialidadRepo;
    }

    public List<Especialidad> listarEspecialidades() {
        try {
            return especialidadRepo.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar especialidades", e);
        }
    }

    public Especialidad crearEspecialidad(Especialidad especialidad) {
        try {
            return especialidadRepo.save(especialidad);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear especialidad", e);
        }
    }

    public Optional<Especialidad> buscarEspecialidadPorId(int id) {
        try {
            return especialidadRepo.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar especialidad por ID", e);
        }
    }

    public void actualizarEspecialidad(Especialidad especialidad) {
        try {
            especialidadRepo.update(especialidad);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar especialidad", e);
        }
    }

    public void eliminarEspecialidad(int id) {
        try {
            especialidadRepo.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar especialidad", e);
        }
    }

    public Optional<Especialidad> crearEspecialidad(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearEspecialidad'");
    }
}