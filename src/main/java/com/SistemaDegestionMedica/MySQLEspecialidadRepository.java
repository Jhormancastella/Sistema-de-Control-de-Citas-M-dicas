package com.SistemaDegestionMedica;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.domain.entities.Especialidad;
import com.SistemaDegestionMedica.infrastructure.database.ConnectionDb;

public class MySQLEspecialidadRepository implements EspecialidadRepository {

    private final ConnectionDb connection;

    public MySQLEspecialidadRepository(ConnectionDb connectionDb) {
        this.connection = connectionDb;
    }

    @Override
    public Especialidad save(Especialidad especialidad) throws SQLException {
        if (especialidad.getId() == null) {
            return insertEspecialidad(especialidad);
        } else {
            updateEspecialidad(especialidad);
            return especialidad;
        }
    }

    @Override
    public Optional<Especialidad> findById(int id) throws SQLException {
        String sql = "SELECT id, nombre FROM especialidades WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEspecialidad(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Especialidad> findAll() throws SQLException {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT id, nombre FROM especialidades";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                especialidades.add(mapToEspecialidad(rs));
            }
        }
        return especialidades;
    }

    @Override
    public void update(Especialidad especialidad) throws SQLException {
        updateEspecialidad(especialidad);
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM especialidades WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Especialidad insertEspecialidad(Especialidad especialidad) throws SQLException {
        String sql = "INSERT INTO especialidades (nombre) VALUES (?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, especialidad.getNombre());
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    especialidad.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para la especialidad");
                }
            }
        }
        return especialidad;
    }

    private void updateEspecialidad(Especialidad especialidad) throws SQLException {
        String sql = "UPDATE especialidades SET nombre = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, especialidad.getNombre());
            stmt.setInt(2, especialidad.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró la especialidad con ID: " + especialidad.getId());
            }
        }
    }

    private Especialidad mapToEspecialidad(ResultSet rs) throws SQLException {
        return new Especialidad(
            rs.getInt("id"),
            rs.getString("nombre")
        );
    }
}