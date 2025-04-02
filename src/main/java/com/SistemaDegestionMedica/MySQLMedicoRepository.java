package com.SistemaDegestionMedica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.domain.entities.Medico;
import com.SistemaDegestionMedica.infrastructure.database.ConnectionDb;

public class MySQLMedicoRepository implements MedicoRepository {
    private final ConnectionDb connectionDb;

    public MySQLMedicoRepository(ConnectionDb connectionDb) {
        this.connectionDb = connectionDb;
    }

    @Override
    public Medico save(Medico medico) {
        String sql = "INSERT INTO medicos (nombre, apellido, especialidad_id) VALUES (?, ?, ?)";
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, medico.getNombre());
            stmt.setString(2, medico.getApellido());
            stmt.setFloat(3, medico.getEspecialidadId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating medico failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medico.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating medico failed, no ID obtained.");
                }
            }
            
            return medico;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving medico", e);
        }
    }

    @Override
    public Optional<Medico> findById(int id) {
        String sql = "SELECT * FROM medicos WHERE id = ?";
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapRowToMedico(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding medico by id", e);
        }
    }

    @Override
    public List<Medico> findAll() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos";
        
        try (Connection connection = connectionDb.getConexion();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                medicos.add(mapRowToMedico(rs));
            }
            return medicos;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all medicos", e);
        }
    }

    @Override
    public void update(Medico medico) {
        String sql = "UPDATE medicos SET nombre = ?, apellido = ?, especialidad_id = ? WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, medico.getNombre());
            stmt.setString(2, medico.getApellido());
            stmt.setInt(3, medico.getEspecialidadId());
            stmt.setInt(4, medico.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating medico", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM medicos WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting medico", e);
        }
    }

    @Override
    public List<Medico> findByEspecialidad(int especialidadId) {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos WHERE especialidad_id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, especialidadId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                medicos.add(mapRowToMedico(rs));
            }
            return medicos;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding medicos by especialidad", e);
        }
    }

    private Medico mapRowToMedico(ResultSet rs) throws SQLException {
        return new Medico(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getInt("especialidad_id")
        );
    }

    @Override
    public List<Medico> findByNombre(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNombre'");
    }
}