package com.SistemaDegestionMedica.adapter.ui;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.SistemaDegestionMedica.application.usecase.EspecialidadService;
import com.SistemaDegestionMedica.application.usecase.MedicoService;
import com.SistemaDegestionMedica.domain.entities.Medico;

public class MedicoMenu {
    private final MedicoService medicoService;
    private final EspecialidadService especialidadService;
    private final Scanner scanner;

    public MedicoMenu(MedicoService medicoService, EspecialidadService especialidadService, Scanner scanner) {
        this.medicoService = medicoService;
        this.especialidadService = especialidadService;
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== GESTIÓN DE MÉDICOS ===");
            System.out.println("1. Registrar nuevo médico");
            System.out.println("2. Listar todos los médicos");
            System.out.println("3. Buscar médico por ID");
            System.out.println("4. Actualizar médico");
            System.out.println("5. Eliminar médico");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    registrarMedico();
                    break;
                case 2:
                    listarMedicos();
                    break;
                case 3:
                    buscarMedicoPorId();
                    break;
                case 4:
                    actualizarMedico();
                    break;
                case 5:
                    eliminarMedico();
                    break;
                case 6:
                    return; // Volver al menú principal
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    private void registrarMedico() {
        System.out.println("\n--- REGISTRAR NUEVO MÉDICO ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.println("Especialidades disponibles:");
        especialidadService.listarEspecialidades().forEach(especialidad ->
                System.out.println(especialidad.getId() + ". " + especialidad.getNombre()));

        System.out.print("Ingrese el ID de la especialidad: ");
        int especialidadId = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        System.out.print("Horario de inicio (HH:mm): ");
        String horarioInicio = scanner.nextLine();
        System.out.print("Horario de fin (HH:mm): ");
        String horarioFin = scanner.nextLine();

        Medico medico = new Medico(null, nombre, especialidadId, horarioInicio, horarioFin);
        medico = medicoService.crearMedico(medico);
        System.out.println("Médico registrado con ID: " + medico.getId());
    }

    private void listarMedicos() {
        System.out.println("\n--- LISTA DE MÉDICOS ---");
        List<Medico> medicos = medicoService.listarMedicos();
        if (medicos.isEmpty()) {
            System.out.println("No hay médicos registrados.");
            return;
        }
        for (Medico medico : medicos) {
            System.out.println("ID: " + medico.getId() + ", Nombre: " + medico.getNombre() +
                               ", Especialidad ID: " + medico.getEspecialidadId() +
                               ", Horario: " + medico.getHorarioInicio() + " - " + medico.getHorarioFin());
        }
    }

    private void buscarMedicoPorId() {
        System.out.print("\nIngrese el ID del médico: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Optional<Medico> medicoOpt = medicoService.obtenerMedico(id);
        if (medicoOpt.isEmpty()) {
            System.out.println("Médico no encontrado.");
            return;
        }

        Medico medico = medicoOpt.get();
        System.out.println("\n--- DETALLES DEL MÉDICO ---");
        System.out.println("ID: " + medico.getId());
        System.out.println("Nombre: " + medico.getNombre());
        System.out.println("Especialidad ID: " + medico.getEspecialidadId());
        System.out.println("Horario: " + medico.getHorarioInicio() + " - " + medico.getHorarioFin());
    }

    private void actualizarMedico() {
        System.out.print("\nIngrese el ID del médico a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Optional<Medico> medicoOpt = medicoService.obtenerMedico(id);
        if (medicoOpt.isEmpty()) {
            System.out.println("Médico no encontrado.");
            return;
        }

        Medico medico = medicoOpt.get();
        System.out.println("\n--- ACTUALIZAR MÉDICO ---");
        System.out.print("Nuevo nombre (" + medico.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            medico.setNombre(nombre);
        }

        System.out.print("Nuevo ID de especialidad (" + medico.getEspecialidadId() + "): ");
        String especialidadIdStr = scanner.nextLine();
        if (!especialidadIdStr.isEmpty()) {
            medico.setEspecialidadId(Integer.parseInt(especialidadIdStr));
        }

        System.out.print("Nuevo horario de inicio (" + medico.getHorarioInicio() + "): ");
        String horarioInicio = scanner.nextLine();
        if (!horarioInicio.isEmpty()) {
            medico.setHorarioInicio(horarioInicio);
        }

        System.out.print("Nuevo horario de fin (" + medico.getHorarioFin() + "): ");
        String horarioFin = scanner.nextLine();
        if (!horarioFin.isEmpty()) {
            medico.setHorarioFin(horarioFin);
        }

        medicoService.actualizarMedico(medico);
        System.out.println("Médico actualizado exitosamente.");
    }

    private void eliminarMedico() {
        System.out.print("\nIngrese el ID del médico a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Optional<Medico> medicoOpt = medicoService.obtenerMedico(id);
        if (medicoOpt.isEmpty()) {
            System.out.println("Médico no encontrado.");
            return;
        }

        medicoService.eliminarMedico(id);
        System.out.println("Médico eliminado exitosamente.");
    }
}