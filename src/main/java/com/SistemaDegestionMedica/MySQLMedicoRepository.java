package com.SistemaDegestionMedica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.SistemaDegestionMedica.domain.Medico;
import com.SistemaDegestionMedica.infrastructure.database.ConnectionDb;

public class MySQLMedicoRepository implements MedicoRepository {
    private final ConnectionDb connectionDb;

    public MySQLMedicoRepository(ConnectionDb connectionDb) {
        this.connectionDb = connectionDb;
    }

    @Override
    public Medico save(Medico medico) {
        String sql = "INSERT INTO medicos (nombre, apellido, documento, telefono, email, especialidad_id, horario_inicio, horario_fin) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            setMedicoParameters(stmt, medico);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("No se pudo crear el médico, ninguna fila afectada.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medico.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para el médico.");
                }
            }
            
            return medico;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar médico: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Medico> findById(int id) {
        String sql = "SELECT * FROM medicos WHERE id = ?";
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToMedico(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar médico por ID: " + id, e);
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
            throw new RuntimeException("Error al listar todos los médicos", e);
        }
    }

    @Override
    public void update(Medico medico) {
        String sql = "UPDATE medicos SET nombre = ?, apellido = ?, documento = ?, telefono = ?, " +
                     "email = ?, especialidad_id = ?, horario_inicio = ?, horario_fin = ? WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            setMedicoParameters(stmt, medico);
            stmt.setInt(9, medico.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se encontró ningún médico con ID: " + medico.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar médico: " + medico.getId(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM medicos WHERE id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("No se encontró ningún médico con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar médico: " + id, e);
        }
    }

    @Override
    public List<Medico> findByEspecialidad(int especialidadId) {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos WHERE especialidad_id = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, especialidadId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapRowToMedico(rs));
                }
                return medicos;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar médicos por especialidad: " + especialidadId, e);
        }
    }

    @Override
    public List<Medico> findByNombre(String nombre) {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos WHERE nombre LIKE ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapRowToMedico(rs));
                }
                return medicos;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar médicos por nombre: " + nombre, e);
        }
    }

    @Override
    public void deleteById(int id) {
        this.delete(id); // Reutiliza el método delete existente
    }

    @Override
    public List<Medico> findByNombreAndApellido(String nombre, String apellido) {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos WHERE nombre = ? AND apellido = ?";
        
        try (Connection connection = connectionDb.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapRowToMedico(rs));
                }
                return medicos;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar médicos por nombre y apellido", e);
        }
    }

    private Medico mapRowToMedico(ResultSet rs) throws SQLException {
        Medico medico = new Medico();
        medico.setId(rs.getInt("id"));
        medico.setNombre(rs.getString("nombre"));
        medico.setApellido(rs.getString("apellido"));
        medico.setDocumento(rs.getString("documento"));
        medico.setTelefono(rs.getString("telefono"));
        medico.setEmail(rs.getString("email"));
        medico.setEspecialidadId(rs.getInt("especialidad_id"));
        medico.setHorarioInicio(rs.getString("horario_inicio"));
        medico.setHorarioFin(rs.getString("horario_fin"));
        return medico;
    }

    private void setMedicoParameters(PreparedStatement stmt, Medico medico) throws SQLException {
        stmt.setString(1, medico.getNombre());
        stmt.setString(2, medico.getApellido());
        stmt.setString(3, medico.getDocumento());
        stmt.setString(4, medico.getTelefono());
        stmt.setString(5, medico.getEmail());
        stmt.setInt(6, medico.getEspecialidadId());
        stmt.setString(7, medico.getHorarioInicio());
        stmt.setString(8, medico.getHorarioFin());
    }
}