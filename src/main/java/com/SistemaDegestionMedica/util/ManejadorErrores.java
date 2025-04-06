package com.SistemaDegestionMedica.util;

import java.util.List;
import java.util.function.Supplier;

import com.SistemaDegestionMedica.domain.entities.Cita;

public class ManejadorErrores {
    public static void manejarError(Runnable operacion, String mensajeExito) {
        try {
            operacion.run();
            System.out.println(mensajeExito);
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Por favor, intente nuevamente.");
        }
    }

    public static <T> T manejarError(Supplier<T> operacion, String mensajeError) {
        try {
            return operacion.get();
        } catch (RuntimeException e) {
            System.err.println(mensajeError + ": " + e.getMessage());
            return null;
        }
    }

    public static List<Cita> manejarError(Runnable runnable, String string, String string2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'manejarError'");
    }
}