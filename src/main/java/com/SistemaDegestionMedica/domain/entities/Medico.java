package com.SistemaDegestionMedica.domain.entities;

public class Medico {
    private int id;
    private String nombre;
    private String especialidad;
    private String telefono;
    private String email;

    // Constructor vacío
    public Medico() {
    }

    // Constructor con todos los atributos
    public Medico(int id, String nombre, String especialidad, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.email = email;
    }

    public Medico(Object object, String nombre2, int especialidadId, String horarioInicio, String horarioFin) {
        //TODO Auto-generated constructor stub
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
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

    public String getHorarioInicio() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHorarioInicio'");
    }

    public void setEspecialidadId(int int1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEspecialidadId'");
    }

    public void setHorarioInicio(String horarioInicio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setHorarioInicio'");
    }

    public String getHorarioFin() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHorarioFin'");
    }

    public void setHorarioFin(String horarioFin) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setHorarioFin'");
    }

    public float getEspecialidadId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEspecialidadId'");
    }

    public String getApellido() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getApellido'");
    }
}
