package com.SistemaDegestionMedica;

import java.sql.Connection;
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
    private final ConnectionDb connectionDb;

    public MySQLEspecialidadRepository(ConnectionDb connectionDb) {
        this.connectionDb = connectionDb;
    }

    @Override
    public Especialidad save(Especialidad especialidad) {
        String sql = "INSERT INTO especialidades (nombre, descripcion) VALUES (?, ?)";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, especialidad.getNombre());
            stmt.setString(2, especialidad.getDescripcion());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating especialidad failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    especialidad.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating especialidad failed, no ID obtained.");
                }
            }
            
            return especialidad;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving especialidad", e);
        }
    }

    @Override
    public Optional<Especialidad> findById(int id) {
        String sql = "SELECT * FROM especialidades WHERE id = ?";
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapRowToEspecialidad(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding especialidad by id", e);
        }
    }

    @Override
    public List<Especialidad> findAll() {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT * FROM especialidades";
        
        try (Connection connection = connectionDb.getConexion();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                especialidades.add(mapRowToEspecialidad(rs));
            }
            return especialidades;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all especialidades", e);
        }
    }

    @Override
    public void update(Especialidad especialidad) {
        String sql = "UPDATE especialidades SET nombre = ?, descripcion = ? WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, especialidad.getNombre());
            stmt.setString(2, especialidad.getDescripcion());
            stmt.setFloat(3, especialidad.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating especialidad", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM especialidades WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting especialidad", e);
        }
    }

    private Especialidad mapRowToEspecialidad(ResultSet rs) throws SQLException {
        return new Especialidad(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("descripcion")
        );
    }
}