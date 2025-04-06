package com.SistemaDegestionMedica.adapter.ui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.SistemaDegestionMedica.application.usecase.CitaService;
import com.SistemaDegestionMedica.application.usecase.MedicoService;
import com.SistemaDegestionMedica.application.usecase.PacienteService;
import com.SistemaDegestionMedica.domain.entities.Cita;
import com.SistemaDegestionMedica.util.Validador;

public class CitaMenu {
    private final CitaService citaService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final Scanner scanner;

    public CitaMenu(CitaService citaService, PacienteService pacienteService, MedicoService medicoService) {
        this.citaService = citaService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== GESTIÓN DE CITAS ===");
            System.out.println("1. Agendar nueva cita");
            System.out.println("2. Listar todas las citas");
            System.out.println("3. Buscar cita por ID");
            System.out.println("4. Reprogramar cita");
            System.out.println("5. Cancelar cita");
            System.out.println("6. Listar citas por paciente");
            System.out.println("7. Listar citas por médico");
            System.out.println("8. Listar citas por estado");
            System.out.println("9. Volver al menú principal");
            
            int opcion = Validador.validarEntero("Seleccione una opción: ", 1, 9);

            switch (opcion) {
                case 1:
                    agendarCita();
                    break;
                case 2:
                    listarTodasLasCitas();
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
                    listarCitasPorPaciente();
                    break;
                case 7:
                    listarCitasPorMedico();
                    break;
                case 8:
                    listarCitasPorEstado();
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
            Validador.presioneParaContinuar();
            // Limpiar la consola después de cada opción
            System.out.print("\033[H\033[2J"); // ANSI escape code to clear the console
            System.out.flush();
            // Esperar a que el usuario presione Enter para continuar
            System.out.println("\nPresione Enter para continuar...");
            scanner.nextLine();
        }
    }

    private void agendarCita() {
        try {
            System.out.println("\n--- AGENDAR NUEVA CITA ---");
            
            // Mostrar pacientes disponibles

            System.out.println("\nPacientes registrados:");
            pacienteService.listarPacientes().forEach(p -> 
                System.out.printf("ID: %d - %s %s%n", p.getId(), p.getNombre(), p.getApellido()));
            
            int pacienteId = Validador.validarEntero("\nIngrese el ID del paciente: ");
            
            // Mostrar médicos disponibles
            
            System.out.println("\nMédicos disponibles:");
            medicoService.listarMedicos().forEach(m -> 
                System.out.printf("ID: %d - %s %s (Especialidad: %s)%n", 
                    m.getId(), m.getNombre(), m.getApellido(), m.getEspecialidadId()));
            
            int medicoId = Validador.validarEntero("\nIngrese el ID del médico: ");
            
            LocalDateTime fechaHora = Validador.validarFechaHora("Ingrese fecha y hora de la cita (YYYY-MM-DD HH:mm): ");
            
            Cita nuevaCita = new Cita();
            nuevaCita.setPacienteId(pacienteId);
            nuevaCita.setMedicoId(medicoId);
            nuevaCita.setFechaHora(fechaHora);
            
            Cita citaAgendada = citaService.agendarCita(nuevaCita);
            System.out.printf("\nCita agendada exitosamente con ID: %d%n", citaAgendada.getId());
            
        } catch (Exception e) {
            System.err.println("\nError al agendar cita: " + e.getMessage());
        }
    }

    private void listarTodasLasCitas() {
        try {
            System.out.println("\n--- LISTADO DE TODAS LAS CITAS ---");
            List<Cita> citas = citaService.listarCitas();
            
            if (citas.isEmpty()) {
                System.out.println("No hay citas registradas.");
                return;
            }
            
            citas.forEach(this::imprimirDetallesCita);
        } catch (Exception e) {
            System.err.println("\nError al listar citas: " + e.getMessage());
        }
    }

    private void buscarCitaPorId() {
        try {
            int id = Validador.validarEntero("\nIngrese el ID de la cita: ");
            
            Optional<Cita> citaOpt = citaService.obtenerCita(id);
            if (citaOpt.isEmpty()) {
                System.out.println("\nNo se encontró una cita con el ID especificado.");
                return;
            }
            
            System.out.println("\n--- DETALLES DE LA CITA ---");
            imprimirDetallesCita(citaOpt.get());
        } catch (Exception e) {
            System.err.println("\nError al buscar cita: " + e.getMessage());
        }
    }

    private void reprogramarCita() {
        try {
            int id = Validador.validarEntero("\nIngrese el ID de la cita a reprogramar: ");
            
            LocalDateTime nuevaFechaHora = Validador.validarFechaHora("Ingrese la nueva fecha y hora (YYYY-MM-DD HH:mm): ");
            
            Cita citaActualizada = citaService.reprogramarCita(id, nuevaFechaHora);
            System.out.printf("\nCita reprogramada exitosamente. Nuevo horario: %s%n", 
                citaActualizada.getFechaHora().toString());
        } catch (Exception e) {
            System.err.println("\nError al reprogramar cita: " + e.getMessage());
        }
    }

    private void cancelarCita() {
        try {
            int id = Validador.validarEntero("\nIngrese el ID de la cita a cancelar: ");
            
            boolean cancelada = citaService.cancelarCita(id);
            if (cancelada) {
                System.out.println("\nCita cancelada exitosamente.");
            } else {
                System.out.println("\nNo se pudo cancelar la cita. Verifique el ID.");
            }
        } catch (Exception e) {
            System.err.println("\nError al cancelar cita: " + e.getMessage());
        }
    }

    private void listarCitasPorPaciente() {
        try {
            System.out.println("\n--- LISTAR CITAS POR PACIENTE ---");
            pacienteService.listarPacientes().forEach(p -> 
                System.out.printf("ID: %d - %s %s%n", p.getId(), p.getNombre(), p.getApellido()));
            
            int pacienteId = Validador.validarEntero("\nIngrese el ID del paciente: ");
            
            List<Cita> citas = citaService.listarCitasPorPaciente(pacienteId);
            
            if (citas.isEmpty()) {
                System.out.println("\nEl paciente no tiene citas registradas.");
                return;
            }
            
            System.out.printf("\nCitas del paciente (ID: %d):%n", pacienteId);
            citas.forEach(this::imprimirDetallesCita);
        } catch (Exception e) {
            System.err.println("\nError al listar citas por paciente: " + e.getMessage());
        }
    }

    private void listarCitasPorMedico() {
        try {
            System.out.println("\n--- LISTAR CITAS POR MÉDICO ---");
            medicoService.listarMedicos().forEach(m -> 
                System.out.printf("ID: %d - %s %s%n", m.getId(), m.getNombre(), m.getApellido()));
            
            int medicoId = Validador.validarEntero("\nIngrese el ID del médico: ");
            
            List<Cita> citas = citaService.listarCitasPorMedico(medicoId);
            
            if (citas.isEmpty()) {
                System.out.println("\nEl médico no tiene citas registradas.");
                return;
            }
            
            System.out.printf("\nCitas del médico (ID: %d):%n", medicoId);
            citas.forEach(this::imprimirDetallesCita);
        } catch (Exception e) {
            System.err.println("\nError al listar citas por médico: " + e.getMessage());
        }
    }

    private void listarCitasPorEstado() {
        try {
            System.out.println("\n--- LISTAR CITAS POR ESTADO ---");
            System.out.println("Estados disponibles: Programada, Confirmada, Cancelada, Completada");
            System.out.print("Ingrese el estado: ");
            String estado = scanner.nextLine();
            
            List<Cita> citas = citaService.listarCitasPorEstado(estado);
            
            if (citas.isEmpty()) {
                System.out.printf("\nNo hay citas con estado '%s'.%n", estado);
                return;
            }
            
            System.out.printf("\nCitas con estado '%s':%n", estado);
            citas.forEach(this::imprimirDetallesCita);
        } catch (Exception e) {
            System.err.println("\nError al listar citas por estado: " + e.getMessage());
        }
    }

    private void imprimirDetallesCita(Cita cita) {
        System.out.printf(
            "ID: %d | Paciente ID: %d | Médico ID: %d | Fecha/Hora: %s | Estado: %s%n",
            cita.getId(),
            cita.getPacienteId(),
            cita.getMedicoId(),
            cita.getFechaHora().toString(),
            cita.getEstado()
        );
    }
}