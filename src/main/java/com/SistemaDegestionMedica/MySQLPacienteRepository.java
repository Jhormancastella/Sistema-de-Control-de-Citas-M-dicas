package com.SistemaDegestionMedica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.domain.entities.Paciente;
import com.SistemaDegestionMedica.infrastructure.database.ConnectionDb;

public class MySQLPacienteRepository implements PacienteRepository {
    private final ConnectionDb connectionDb;

    public MySQLPacienteRepository(ConnectionDb connectionDb) {
        this.connectionDb = connectionDb;
    }

    @Override
    public Paciente save(Paciente paciente) {
        String sql = "INSERT INTO pacientes (documento, nombre, apellido, fecha_nacimiento, genero, direccion, telefono, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, paciente.getDocumento());
            stmt.setString(2, paciente.getNombre());
            stmt.setString(3, paciente.getApellido());
            stmt.setDate(4, Date.valueOf(paciente.getFechaNacimiento()));
            stmt.setString(5, paciente.getGenero());
            stmt.setString(6, paciente.getDireccion());
            stmt.setString(7, paciente.getTelefono());
            stmt.setString(8, paciente.getEmail());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating paciente failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paciente.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating paciente failed, no ID obtained.");
                }
            }
            
            return paciente;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving paciente", e);
        }
    }

    @Override
    public Optional<Paciente> findById(int id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapRowToPaciente(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding paciente by id", e);
        }
    }

    @Override
    public List<Paciente> findAll() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";
        
        try (Connection connection = connectionDb.getConexion();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pacientes.add(mapRowToPaciente(rs));
            }
            return pacientes;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all pacientes", e);
        }
    }

    @Override
    public void update(Paciente paciente) {
        String sql = "UPDATE pacientes SET documento = ?, nombre = ?, apellido = ?, fecha_nacimiento = ?, genero = ?, direccion = ?, telefono = ?, email = ? WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, paciente.getDocumento());
            stmt.setString(2, paciente.getNombre());
            stmt.setString(3, paciente.getApellido());
            stmt.setDate(4, Date.valueOf(paciente.getFechaNacimiento()));
            stmt.setString(5, paciente.getGenero());
            stmt.setString(6, paciente.getDireccion());
            stmt.setString(7, paciente.getTelefono());
            stmt.setString(8, paciente.getEmail());
            stmt.setInt(9, paciente.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating paciente", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting paciente", e);
        }
    }

    @Override
    public Optional<Paciente> findByDocumento(String documento) {
        String sql = "SELECT * FROM pacientes WHERE documento = ?";
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, documento);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapRowToPaciente(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding paciente by documento", e);
        }
    }

    public ConnectionDb getConnectionDb() {
        return connectionDb;
    }

    private Paciente mapRowToPaciente(ResultSet rs) throws SQLException {
        return new Paciente(
            rs.getInt("id"),
            rs.getString("documento"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getDate("fecha_nacimiento").toLocalDate(),
            rs.getString("genero"),
            rs.getString("direccion"),
            rs.getString("telefono"),
            rs.getString("email")
        );
    }
}