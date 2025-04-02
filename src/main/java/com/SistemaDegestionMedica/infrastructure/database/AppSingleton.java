package com.SistemaDegestionMedica.infrastructure.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public enum AppSingleton {
    INSTANCIA;

    private final Properties propiedades = new Properties();

    AppSingleton() {
        cargarConfiguraciones("config.properties");
    }

    private void cargarConfiguraciones(String rutaArchivo) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (FileInputStream archivo = new FileInputStream(classLoader.getResource(rutaArchivo).getFile())) {
            propiedades.load(archivo);
        } catch (IOException e) {
            System.err.println("❌ Error cargando configuración: " + e.getMessage());
        }
    }

    public String get(String clave) {
        return propiedades.getProperty(clave, "No encontrado");
    }
}
