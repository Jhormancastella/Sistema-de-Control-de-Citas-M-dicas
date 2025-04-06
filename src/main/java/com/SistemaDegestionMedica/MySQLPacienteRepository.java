package com.SistemaDegestionMedica;

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
    private final ConnectionDb connection;

    public MySQLPacienteRepository(ConnectionDb connectionDb) {
        this.connection = connectionDb;
    }

    @Override
public Paciente save(Paciente paciente) throws SQLException {
        if (paciente.getId() == 0) {
            return insertPaciente(paciente);
        } else {
            update(paciente);
            return paciente;
        }
    }
    @Override
    public Optional<Paciente> findById(int id) throws SQLException {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToPaciente(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Paciente> findAll() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pacientes.add(mapToPaciente(rs));
            }
        }
        return pacientes;
    }

    @Override
    public void update(Paciente paciente) throws SQLException {
        String sql = "UPDATE pacientes SET nombre = ?, apellido = ?, documento = ?, telefono = ?, email = ?, fecha_nacimiento = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellido());
            stmt.setString(3, paciente.getDocumento());
            stmt.setString(4, paciente.getTelefono());
            stmt.setString(5, paciente.getEmail());
            stmt.setDate(6, java.sql.Date.valueOf(paciente.getFechaNacimiento()));
            stmt.setInt(7, paciente.getId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Paciente insertPaciente(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO pacientes (nombre, apellido, documento, telefono, email, fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellido());
            stmt.setString(3, paciente.getDocumento());
            stmt.setString(4, paciente.getTelefono());
            stmt.setString(5, paciente.getEmail());
            stmt.setDate(6, java.sql.Date.valueOf(paciente.getFechaNacimiento()));
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paciente.setId(generatedKeys.getInt(1));
                }
            }
        }
        return paciente;
    }

    private Paciente mapToPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setId(rs.getInt("id"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setApellido(rs.getString("apellido"));
        paciente.setDocumento(rs.getString("documento"));
        paciente.setTelefono(rs.getString("telefono"));
        paciente.setEmail(rs.getString("email"));
        paciente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        return paciente;
    }
}