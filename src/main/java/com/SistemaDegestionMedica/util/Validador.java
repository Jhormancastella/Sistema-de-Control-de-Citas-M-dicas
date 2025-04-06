package com.SistemaDegestionMedica.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Validador {
    private static final Scanner scanner = new Scanner(System.in);

    // Validación para números enteros con rango
    public static int validarEntero(String mensaje, int min, int max) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine();
                int valor = Integer.parseInt(input);
                
                if (valor < min || valor > max) {
                    System.out.printf("Error: Debe ingresar un número entre %d y %d.%n", min, max);
                    continue;
                }
                
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido.");
            }
        }
    }

    // Validación para números enteros sin rango
    public static int validarEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido.");
            }
        }
    }

    // Validación para fechas (LocalDate)
    public static LocalDate validarFecha(String mensaje) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            try {
                System.out.print(mensaje + " (YYYY-MM-DD): ");
                String input = scanner.nextLine();
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha inválido. Use YYYY-MM-DD.");
            }
        }
    }

    // Validación para fechas con hora (LocalDateTime)
    public static LocalDateTime validarFechaHora(String mensaje) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        while (true) {
            try {
                System.out.print(mensaje + " (YYYY-MM-DD HH:mm): ");
                String input = scanner.nextLine();
                return LocalDateTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha/hora inválido. Use YYYY-MM-DD HH:mm.");
            }
        }
    }

    // Validación para texto (solo letras y espacios)
    public static String validarTexto(String mensaje) {
        Pattern patron = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim();
            if (patron.matcher(input).matches()) {
                return input;
            }
            System.out.println("Error: Solo se permiten letras y espacios.");
        }
    }

    // Validación para emails
    public static String validarEmail(String mensaje) {
        Pattern patron = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim();
            if (patron.matcher(input).matches()) {
                return input;
            }
            System.out.println("Error: Formato de email inválido.");
        }
    }

    // Validación para teléfonos
    public static String validarTelefono(String mensaje) {
        Pattern patron = Pattern.compile("^[0-9]{7,15}$");
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim();
            if (patron.matcher(input).matches()) {
                return input;
            }
            System.out.println("Error: Formato de teléfono inválido (solo números, 7-15 dígitos).");
        }
    }

    public static void presioneParaContinuar() {
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }
}