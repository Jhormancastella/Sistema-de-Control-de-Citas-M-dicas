package com.SistemaDegestionMedica;

public class CitaEstado {
    private String estado;

    public CitaEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "CitaEstado{" +
                "estado='" + estado + '\'' +
                '}';
    }
}
