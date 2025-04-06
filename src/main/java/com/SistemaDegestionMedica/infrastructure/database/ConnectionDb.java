package com.SistemaDegestionMedica.infrastructure.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public interface ConnectionDb {
    Connection getConexion() throws SQLException;

    boolean testConnection();

    PreparedStatement prepareStatement(String sql) throws SQLException;

    Statement createStatement() throws SQLException;

    PreparedStatement prepareStatement(String sql, int returnGeneratedKeys) throws SQLException;
}