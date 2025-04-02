package com.SistemaDegestionMedica;

import java.util.Scanner;

import com.SistemaDegestionMedica.adapter.ui.CitaMenu;
import com.SistemaDegestionMedica.adapter.ui.EspecialidadMenu;
import com.SistemaDegestionMedica.adapter.ui.MedicoMenu;
import com.SistemaDegestionMedica.adapter.ui.PacienteMenu;
import com.SistemaDegestionMedica.application.usecase.CitaService;
import com.SistemaDegestionMedica.application.usecase.EspecialidadService;
import com.SistemaDegestionMedica.application.usecase.MedicoService;
import com.SistemaDegestionMedica.application.usecase.PacienteService;
import com.SistemaDegestionMedica.infrastructure.database.ConnectionDb;
import com.SistemaDegestionMedica.infrastructure.database.ConnectionFactory;



public class MainMenu {
    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final EspecialidadService especialidadService;
    private final CitaService citaService;
    private final Scanner scanner;

    public MainMenu() {
        // Crear conexión a la base de datos
        final ConnectionDb connectionDb = ConnectionFactory.creaConnection();
        
        // Test the connection
        if (!connectionDb.testConnection()) {
            System.err.println("ERROR: No se pudo conectar a la base de datos");
            System.err.println("Verifique que:");
            System.err.println("1. MySQL esté corriendo en localhost:3306");
            System.err.println("2. La base de datos 'Eps' exista");
            System.err.println("3. Las credenciales sean correctas (usuario: root, password: campus2023)");
            System.exit(1);
        }

        // Inicializar repositorios
        PacienteRepository pacienteRepo = new MySQLPacienteRepository(connectionDb);
        MedicoRepository medicoRepo = new MySQLMedicoRepository(connectionDb);
        EspecialidadRepository especialidadRepo = new MySQLEspecialidadRepository(connectionDb);
        CitaRepository citaRepo = new MySQLCitaRepository(connectionDb);

        // Inicializar servicios
        this.pacienteService = new PacienteService(pacienteRepo);
        this.medicoService = new MedicoService(medicoRepo);
        this.especialidadService = new EspecialidadService(especialidadRepo);
        this.citaService = new CitaService(citaRepo, pacienteRepo, medicoRepo); // Asumiendo que CitaService necesita estos repositorios

        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== SISTEMA DE CITAS MÉDICAS ===");
            System.out.println("1. Gestión de Pacientes");
            System.out.println("2. Gestión de Médicos");
            System.out.println("3. Gestión de Especialidades");
            System.out.println("4. Gestión de Citas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    new PacienteMenu(pacienteService, scanner).mostrarMenu();
                    break;
                case 2:
                    new MedicoMenu(medicoService, especialidadService, scanner).mostrarMenu();
                    break;
                case 3:
                    new EspecialidadMenu(especialidadService, scanner).mostrarMenu();
                    break;
                case 4:
                    new CitaMenu(citaService, pacienteService, medicoService, scanner).mostrarMenu();
                    break;
                case 5:
                    System.out.println("Saliendo del sistema...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            new MainMenu().mostrarMenu();
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}