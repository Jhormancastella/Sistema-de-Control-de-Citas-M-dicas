package com.SistemaDegestionMedica.infrastructure.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.SistemaDegestionMedica.config.HexaSingleton;

public class ConnMySql implements ConnectionDb {
    @Override
    public Connection getConexion() throws SQLException {
        HexaSingleton config = HexaSingleton.INSTANCIA;
        String url = config.get("db.url");
        String usuario = config.get("db.user");
        String password = config.get("db.password");

        return DriverManager.getConnection(url, usuario, password);
    }
    
    @Override
    public boolean testConnection() {
        try {
            Connection connection = getConexion();
            boolean isValid = connection != null && !connection.isClosed();
            if (isValid) {
                connection.close();
            }
            return isValid;
        } catch (SQLException e) {
            System.err.println("Error al probar la conexión: " + e.getMessage());
            return false;
        }
    }
}