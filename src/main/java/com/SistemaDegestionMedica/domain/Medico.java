package com.SistemaDegestionMedica.domain;

public class Medico {
    private Integer id;
    private String nombre;
    private String apellido;
    private String documento;
    private String telefono;
    private String email;
    private Integer especialidadId;  // Cambiado de Especialidad a Integer para el ID
    private String horarioInicio;
    private String horarioFin;

    // Constructores
    public Medico() {}

    public Medico(Integer id, String nombre, Integer especialidadId, String horarioInicio, String horarioFin) {
        this.id = id;
        this.nombre = nombre;
        this.especialidadId = especialidadId;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(Integer especialidadId) {
        this.especialidadId = especialidadId;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(String horarioFin) {
        this.horarioFin = horarioFin;
    }
}