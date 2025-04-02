package com.SistemaDegestionMedica.adapter.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.SistemaDegestionMedica.application.usecase.CitaService;
import com.SistemaDegestionMedica.application.usecase.MedicoService;
import com.SistemaDegestionMedica.application.usecase.PacienteService;
import com.SistemaDegestionMedica.domain.entities.Cita;

public class CitaMenu {
    private final CitaService citaService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final Scanner scanner;

    public CitaMenu(CitaService citaService, PacienteService pacienteService, MedicoService medicoService, Scanner scanner) {
        this.citaService = citaService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== GESTIÓN DE CITAS ===");
            System.out.println("1. Agendar nueva cita");
            System.out.println("2. Listar todas las citas");
            System.out.println("3. Buscar cita por ID");
            System.out.println("4. Reprogramar cita");
            System.out.println("5. Cancelar cita");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    agendarCita();
                    break;
                case 2:
                    listarCitas();
                    break;
                case 3:
                    buscarCitaPorId();
                    break;
                case 4:
                    reprogramarCita();
                    break;
                case 5:
                    cancelarCita();
                    break;
                case 6:
                    return; // Volver al menú principal
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    private void agendarCita() {
        System.out.println("\n--- AGENDAR NUEVA CITA ---");

        // Seleccionar paciente
        System.out.print("Ingrese el ID del paciente: ");
        int pacienteId = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea
        if (pacienteService.obtenerPaciente(pacienteId).isEmpty()) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        // Seleccionar médico
        System.out.print("Ingrese el ID del médico: ");
        int medicoId = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea
        if (medicoService.obtenerMedico(medicoId).isEmpty()) {
            System.out.println("Médico no encontrado.");
            return;
        }

        // Fecha y hora de la cita
        System.out.print("Fecha y hora de la cita (YYYY-MM-DD HH:mm): ");
        String fechaHoraStr = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime fechaHora;
        try {
            fechaHora = LocalDateTime.parse(fechaHoraStr, formatter);
        } catch (Exception e) {
            System.out.println("Formato de fecha y hora inválido. Use el formato YYYY-MM-DD HH:mm.");
            return;
        }

        // Crear cita
        Cita cita = new Cita(null, pacienteId, medicoId, fechaHora, "Programada");
        cita = citaService.agendarCita(cita);
        System.out.println("Cita agendada con ID: " + cita.getId());
    }

    private void listarCitas() {
        System.out.println("\n--- LISTA DE CITAS ---");
        List<Cita> citas = citaService.listarCitas();
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.");
            return;
        }
        for (Cita cita : citas) {
            System.out.println("ID: " + cita.getId() +
                               ", Paciente ID: " + cita.getPacienteId() +
                               ", Médico ID: " + cita.getMedicoId() +
                               ", Fecha/Hora: " + cita.getFechaHora() +
                               ", Estado: " + cita.getEstado());
        }
    }

    private void buscarCitaPorId() {
        System.out.print("\nIngrese el ID de la cita: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Optional<Cita> citaOpt = citaService.obtenerCita(id);
        if (citaOpt.isEmpty()) {
            System.out.println("Cita no encontrada.");
            return;
        }

        Cita cita = citaOpt.get();
        System.out.println("\n--- DETALLES DE LA CITA ---");
        System.out.println("ID: " + cita.getId());
        System.out.println("Paciente ID: " + cita.getPacienteId());
        System.out.println("Médico ID: " + cita.getMedicoId());
        System.out.println("Fecha/Hora: " + cita.getFechaHora());
        System.out.println("Estado: " + cita.getEstado());
    }

    private void reprogramarCita() {
        System.out.print("\nIngrese el ID de la cita a reprogramar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Optional<Cita> citaOpt = citaService.obtenerCita(id);
        if (citaOpt.isEmpty()) {
            System.out.println("Cita no encontrada.");
            return;
        }

        System.out.print("Nueva fecha y hora (YYYY-MM-DD HH:mm): ");
        String nuevaFechaHoraStr = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime nuevaFechaHora;
        try {
            nuevaFechaHora = LocalDateTime.parse(nuevaFechaHoraStr, formatter);
        } catch (Exception e) {
            System.out.println("Formato de fecha y hora inválido. Use el formato YYYY-MM-DD HH:mm.");
            return;
        }

        Cita cita = citaService.reprogramarCita(id, nuevaFechaHora);
        if (cita != null) {
            System.out.println("Cita reprogramada exitosamente.");
        } else {
            System.out.println("Error al reprogramar la cita.");
        }
    }

    private void cancelarCita() {
        System.out.print("\nIngrese el ID de la cita a cancelar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Optional<Cita> citaOpt = citaService.obtenerCita(id);
        if (citaOpt.isEmpty()) {
            System.out.println("Cita no encontrada.");
            return;
        }

        boolean cancelacionExitosa = citaService.cancelarCita(id);
        if (cancelacionExitosa) {
            System.out.println("Cita cancelada exitosamente.");
        } else {
            System.out.println("Error al cancelar la cita.");
        }
    }

    public String getPacienteId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPacienteId'");
    }

    public String getMedicoId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMedicoId'");
    }

    public LocalDateTime getFechaHora() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFechaHora'");
    }

    public Object getEstado() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEstado'");
    }
}
