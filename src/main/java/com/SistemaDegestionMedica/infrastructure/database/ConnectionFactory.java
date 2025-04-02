package com.SistemaDegestionMedica.infrastructure.database;

import java.sql.Connection;

public class ConnectionFactory {

    public static ConnectionDb creaConnection() {
        return new ConnMySql();
    }
    public Connection getConnection() {
        throw new UnsupportedOperationException("Unimplement method 'getConnection");
    }
    
}