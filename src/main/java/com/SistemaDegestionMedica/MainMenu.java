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
    private boolean primeraVez = true;

    public MainMenu() {
        ConnectionDb connectionDb = inicializarConexion();
        this.pacienteService = inicializarPacienteService(connectionDb);
        this.medicoService = inicializarMedicoService(connectionDb);
        this.especialidadService = inicializarEspecialidadService(connectionDb);
        this.citaService = inicializarCitaService(connectionDb);
        this.scanner = new Scanner(System.in);
    }
    
    private ConnectionDb inicializarConexion() {
        final ConnectionDb connectionDb = ConnectionFactory.creaConnection();
        if (!connectionDb.testConnection()) {
            System.err.println("ERROR: No se pudo conectar a la base de datos");
            System.err.println("Verifique que:");
            System.err.println("1. MySQL esté corriendo en localhost:3306");
            System.err.println("2. La base de datos 'Eps' exista");
            System.err.println("3. Las credenciales sean correctas (usuario: root, password: campus2023)");
            System.exit(1);
        }
        return connectionDb;
    }

    private PacienteService inicializarPacienteService(ConnectionDb connectionDb) {
        PacienteRepository pacienteRepo = new MySQLPacienteRepository(connectionDb);
        return new PacienteService(pacienteRepo);
    }

    private MedicoService inicializarMedicoService(ConnectionDb connectionDb) {
        MedicoRepository medicoRepo = new MySQLMedicoRepository(connectionDb);
        return new MedicoService(medicoRepo);
    }

    private EspecialidadService inicializarEspecialidadService(ConnectionDb connectionDb) {
        EspecialidadRepository especialidadRepo = new MySQLEspecialidadRepository(connectionDb);
        return new EspecialidadService(especialidadRepo);
    }

    private CitaService inicializarCitaService(ConnectionDb connectionDb) {
        CitaRepository citaRepo = new MySQLCitaRepository(connectionDb);
        return new CitaService(citaRepo, new MySQLPacienteRepository(connectionDb), new MySQLMedicoRepository(connectionDb));
    }

    public void mostrarMenu() {
        while (true) {
            limpiarConsola();
            if (primeraVez) {
                System.out.println("Conexión a la base de datos 'Eps' establecida correctamente con el usuario 'root'.");
                primeraVez = false;
            }
            System.out.println("\n=== SISTEMA DE CITAS MÉDICAS ===");
            System.out.println("1. Gestión de Pacientes");
            System.out.println("2. Gestión de Médicos");
            System.out.println("3. Gestión de Especialidades");
            System.out.println("4. Gestión de Citas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); 

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

    public static void limpiarConsola() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Para Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Para Unix/Linux/Mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.err.println("Error al limpiar la consola: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            new MainMenu().mostrarMenu();
        } catch (Exception e) {
            System.err.println("Se produjo un error inesperado:");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}