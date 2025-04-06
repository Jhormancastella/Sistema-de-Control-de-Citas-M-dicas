package com.SistemaDegestionMedica.adapter.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.SistemaDegestionMedica.application.usecase.PacienteService;
import com.SistemaDegestionMedica.domain.entities.Paciente;

public class PacienteMenu {
    private final PacienteService pacienteService;
    private final Scanner scanner;

    public PacienteMenu(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
        this.scanner = new Scanner(System.in); // Inicialización correcta del Scanner
    // scanner = new Scanner(System.in); // Inicialización correcta del Scanner
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== GESTIÓN DE PACIENTES ===");
            System.out.println("1. Registrar nuevo paciente");
            System.out.println("2. Listar todos los pacientes");
            System.out.println("3. Buscar paciente por ID");
            System.out.println("4. Actualizar paciente");
            System.out.println("5. Eliminar paciente");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    registrarPaciente();
                    break;
                case 2:
                    listarPacientes();
                    break;
                case 3:
                    buscarPacientePorId();
                    break;
                case 4:
                    actualizarPaciente();
                    break;
                case 5:
                    eliminarPaciente();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    private void registrarPaciente() {
        System.out.println("\n--- REGISTRAR NUEVO PACIENTE ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Documento: ");
        String documento = scanner.nextLine();
        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        LocalDate fechaNacimiento = LocalDate.parse(scanner.nextLine());
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
    
        Paciente nuevoPaciente = new Paciente();
        nuevoPaciente.setNombre(nombre);
        nuevoPaciente.setApellido(apellido);
        nuevoPaciente.setDocumento(documento);
        nuevoPaciente.setFechaNacimiento(fechaNacimiento);
        nuevoPaciente.setTelefono(telefono);
        nuevoPaciente.setEmail(email);
    
        pacienteService.registrarPaciente(nuevoPaciente);
        System.out.println("Paciente registrado exitosamente.");
    }

    private void listarPacientes() {
        System.out.println("\n--- LISTA DE PACIENTES ---");
        List<Paciente> pacientes = pacienteService.listarPacientes();
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        for (Paciente paciente : pacientes) {
            System.out.printf("ID: %d, Nombre: %s %s, Tel: %s, Email: %s%n",
                    paciente.getId(), paciente.getNombre(), paciente.getApellido(),
                    paciente.getTelefono(), paciente.getEmail());
        }
    }

    private void buscarPacientePorId() {
        System.out.print("\nIngrese el ID del paciente: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Paciente> pacienteOpt = pacienteService.obtenerPaciente(id);
        if (pacienteOpt.isEmpty()) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        Paciente paciente = pacienteOpt.get();
        System.out.println("\n--- DETALLES DEL PACIENTE ---");
        System.out.println("ID: " + paciente.getId());
        System.out.println("Documento: " + paciente.getDocumento());
        System.out.println("Nombre: " + paciente.getNombre() + " " + paciente.getApellido());
        System.out.println("Fecha de nacimiento: " + paciente.getFechaNacimiento());
        System.out.println("Teléfono: " + paciente.getTelefono());
        System.out.println("Email: " + paciente.getEmail());
    }

    private void actualizarPaciente() {
        System.out.print("\nIngrese el ID del paciente a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Paciente> pacienteOpt = pacienteService.obtenerPaciente(id);
        if (pacienteOpt.isEmpty()) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        Paciente paciente = pacienteOpt.get();
        System.out.println("\n--- ACTUALIZAR PACIENTE ---");
        
        System.out.print("Nuevo documento (" + paciente.getDocumento() + "): ");
        String documento = scanner.nextLine();
        if (!documento.isEmpty()) paciente.setDocumento(documento);

        System.out.print("Nuevo nombre (" + paciente.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) paciente.setNombre(nombre);

        System.out.print("Nuevo apellido (" + paciente.getApellido() + "): ");
        String apellido = scanner.nextLine();
        if (!apellido.isEmpty()) paciente.setApellido(apellido);

        System.out.print("Nueva fecha de nacimiento (" + paciente.getFechaNacimiento() + ", YYYY-MM-DD): ");
        String fechaStr = scanner.nextLine();
        if (!fechaStr.isEmpty()) paciente.setFechaNacimiento(LocalDate.parse(fechaStr));

        System.out.print("Nuevo teléfono (" + paciente.getTelefono() + "): ");
        String telefono = scanner.nextLine();
        if (!telefono.isEmpty()) paciente.setTelefono(telefono);

        System.out.print("Nuevo email (" + paciente.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) paciente.setEmail(email);

        pacienteService.actualizarPaciente(paciente);
        System.out.println("Paciente actualizado exitosamente.");
    }

    private void eliminarPaciente() {
        System.out.print("\nIngrese el ID del paciente a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        pacienteService.eliminarPaciente(id);
        System.out.println("Paciente eliminado exitosamente.");
    }
}