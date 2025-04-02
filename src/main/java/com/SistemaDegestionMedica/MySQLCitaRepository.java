package com.SistemaDegestionMedica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.adapter.ui.CitaMenu;
import com.SistemaDegestionMedica.domain.entities.Cita;
import com.SistemaDegestionMedica.infrastructure.database.ConnectionDb;

public class MySQLCitaRepository implements CitaRepository {
    private final ConnectionDb connectionDb;

    public MySQLCitaRepository(ConnectionDb connectionDb) {
        this.connectionDb = connectionDb;
    }

    @Override
    public Cita save(Cita cita) {
        String sql = "INSERT INTO citas (paciente_id, medico_id, fecha_hora, estado) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, cita.getPacienteId());
            stmt.setInt(2, cita.getMedicoId());
            stmt.setTimestamp(3, Timestamp.valueOf(cita.getFechaHora()));
            stmt.setString(4, cita.getEstado());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating appointment failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cita.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating appointment failed, no ID obtained.");
                }
            }
            
            return cita;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving appointment", e);
        }
    }

    @Override
    public Optional findById(int id) {
        String sql = "SELECT * FROM citas WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapRowToCita(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointment by id", e);
        }
    }

    @Override
    public List<Cita> findAll() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM citas";
        
        try (Connection connection = connectionDb.getConexion();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                citas.add(mapRowToCita(rs));
            }
            return citas;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all appointments", e);
        }
    }

    @Override
    public Cita update(Cita cita) {
        String sql = "UPDATE citas SET paciente_id = ?, medico_id = ?, fecha_hora = ?, estado = ? WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, cita.getPacienteId());
            stmt.setInt(2, cita.getMedicoId());
            stmt.setTimestamp(3, Timestamp.valueOf(cita.getFechaHora()));
            stmt.setString(4, cita.getEstado());
            stmt.setInt(5, cita.getId());
            
            stmt.executeUpdate();
            return cita;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment", e);
        }
    }

    @Override
    public Cita update(CitaMenu citaMenu) {

        Cita cita = new Cita();

        cita.setId(citaMenu.getId());
        cita.setPacienteId(citaMenu.getPacienteId());
        cita.setMedicoId(citaMenu.getMedicoId());
        cita.setFechaHora(citaMenu.getFechaHora());
        cita.setEstado(citaMenu.getEstado());
        
        return update(cita);
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM citas WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment", e);
        }
    }

    @Override
    public boolean isMedicoDisponible(int medicoId, LocalDateTime fechaHora) {
        String sql = "SELECT COUNT(*) FROM citas WHERE medico_id = ? AND fecha_hora = ? AND estado != 'Cancelada'";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, medicoId);
            stmt.setTimestamp(2, Timestamp.valueOf(fechaHora));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking doctor availability", e);
        }
    }

    @Override
    public boolean isMedicoDisponible(String medicoId, LocalDateTime fechaHora) {
        try {
            int id = Integer.parseInt(medicoId);
            return isMedicoDisponible(id, fechaHora);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid doctor ID format", e);
        }
    }

    @Override
    public boolean isMedicoDisponible(int medicoId, String fechaHoraStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, formatter);
            return isMedicoDisponible(medicoId, fechaHora);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format", e);
        }
    }

    @Override
    public List<Cita> findByPacienteId(int pacienteId) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE paciente_id = ? ORDER BY fecha_hora DESC";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, pacienteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                citas.add(mapRowToCita(rs));
            }
            return citas;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointments by patient", e);
        }
    }

    @Override
    public List<Cita> findByMedicoId(int medicoId) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE medico_id = ? ORDER BY fecha_hora DESC";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, medicoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                citas.add(mapRowToCita(rs));
            }
            return citas;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointments by doctor", e);
        }
    }

    private Cita mapRowToCita(ResultSet rs) throws SQLException {
        return new Cita(
            rs.getInt("id"),
            rs.getInt("paciente_id"),
            rs.getInt("medico_id"),
            rs.getTimestamp("fecha_hora").toLocalDateTime(),
            rs.getString("estado")
        );
    }
}