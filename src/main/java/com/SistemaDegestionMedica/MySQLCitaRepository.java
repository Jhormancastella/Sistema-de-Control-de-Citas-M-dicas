package com.SistemaDegestionMedica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                throw new SQLException("No se pudo crear la cita, ninguna fila afectada.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cita.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para la cita.");
                }
            }
            
            return cita;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar cita: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cita> findById(int id) {
        String sql = "SELECT * FROM citas WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCita(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cita por ID: " + id, e);
        }
    }

    @Override
    public List<Cita> findAll() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM citas ORDER BY fecha_hora DESC";
        
        try (Connection connection = connectionDb.getConexion();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                citas.add(mapRowToCita(rs));
            }
            return citas;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todas las citas", e);
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
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró ninguna cita con ID: " + cita.getId());
            }
            return cita;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cita: " + cita.getId(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM citas WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("No se encontró ninguna cita con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cita: " + id, e);
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
            throw new RuntimeException("Error al verificar disponibilidad del médico", e);
        }
    }

    @Override
    public List<Cita> findByPacienteId(int pacienteId) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE paciente_id = ? ORDER BY fecha_hora DESC";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, pacienteId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapRowToCita(rs));
                }
                return citas;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar citas por paciente: " + pacienteId, e);
        }
    }

    @Override
    public List<Cita> findByMedicoId(int medicoId) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE medico_id = ? ORDER BY fecha_hora DESC";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, medicoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapRowToCita(rs));
                }
                return citas;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar citas por médico: " + medicoId, e);
        }
    }

    private Cita mapRowToCita(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setId(rs.getInt("id"));
        cita.setPacienteId(rs.getInt("paciente_id"));
        cita.setMedicoId(rs.getInt("medico_id"));
        cita.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        cita.setEstado(rs.getString("estado"));
        return cita;
    }
}