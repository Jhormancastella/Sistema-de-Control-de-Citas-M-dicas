package com.SistemaDegestionMedica.adapter.ui;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.SistemaDegestionMedica.application.usecase.EspecialidadService;
import com.SistemaDegestionMedica.domain.entities.Especialidad;

public class EspecialidadMenu {
    private final EspecialidadService especialidadService;
    private final Scanner scanner;

    public EspecialidadMenu(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
        this.scanner = new Scanner(System.in); // Inicialización correcta
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== GESTIÓN DE ESPECIALIDADES ===");
            System.out.println("1. Registrar nueva especialidad");
            System.out.println("2. Listar todas las especialidades");
            System.out.println("3. Buscar especialidad por ID");
            System.out.println("4. Actualizar especialidad");
            System.out.println("5. Eliminar especialidad");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1:
                    registrarEspecialidad();
                    break;
                case 2:
                    listarEspecialidades();
                    break;
                case 3:
                    buscarEspecialidadPorId();
                    break;
                case 4:
                    actualizarEspecialidad();
                    break;
                case 5:
                    eliminarEspecialidad();
                    break;
                case 6:
                    return; // Volver al menú principal
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    private void registrarEspecialidad() {
        System.out.println("\n--- REGISTRAR NUEVA ESPECIALIDAD ---");
        System.out.print("Nombre de la especialidad: ");
        String nombre = scanner.nextLine();

        Especialidad especialidad = new Especialidad(null, nombre);
        especialidad = especialidadService.crearEspecialidad(especialidad);
        System.out.println("Especialidad registrada con ID: " + especialidad.getId());
    }

    private void listarEspecialidades() {
        System.out.println("\n--- LISTA DE ESPECIALIDADES ---");
        List<Especialidad> especialidades = especialidadService.listarEspecialidades();
        if (especialidades.isEmpty()) {
            System.out.println("No hay especialidades registradas.");
            return;
        }
        for (Especialidad especialidad : especialidades) {
            System.out.println("ID: " + especialidad.getId() + ", Nombre: " + especialidad.getNombre());
        }
    }

    private void buscarEspecialidadPorId() {
        System.out.print("\nIngrese el ID de la especialidad: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 
    
        Optional<Especialidad> especialidadOpt = especialidadService.buscarEspecialidadPorId(id);
        if (especialidadOpt.isEmpty()) {
            System.out.println("Especialidad no encontrada.");
            return;
        }
    
        Especialidad especialidad = especialidadOpt.get();
        System.out.println("\n--- DETALLES DE LA ESPECIALIDAD ---");
        System.out.println("ID: " + especialidad.getId());
        System.out.println("Nombre: " + especialidad.getNombre());
    }

    private void actualizarEspecialidad() {
        System.out.print("\nIngrese el ID de la especialidad a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 
    
        Optional<Especialidad> especialidadOpt = especialidadService.buscarEspecialidadPorId(id);

        Especialidad especialidad = especialidadOpt.get();
        System.out.println("\n--- ACTUALIZAR ESPECIALIDAD ---");
        System.out.print("Nuevo nombre (" + especialidad.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            especialidad.setNombre(nombre);
        }

        especialidadService.actualizarEspecialidad(especialidad);
        System.out.println("Especialidad actualizada exitosamente.");
    }

    private void eliminarEspecialidad() {
        System.out.print("\nIngrese el ID de la especialidad a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 
    
        Optional<Especialidad> especialidadOpt = especialidadService.buscarEspecialidadPorId(id);
    

        especialidadService.eliminarEspecialidad(id);
        System.out.println("Especialidad eliminada exitosamente.");
    }
}