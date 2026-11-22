package com.opycorp.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "eventos_externos")
public class EventoExterno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String fecha;
    private String comuna;
    private String organizacion;
    private String descripcion;
    private String tipo;
    private String publicoEsperado;

    public EventoExterno() {}

    public EventoExterno(String nombre, String fecha, String comuna, String organizacion, String descripcion, String tipo, String publicoEsperado) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.comuna = comuna;
        this.organizacion = organizacion;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.publicoEsperado = publicoEsperado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }

    public String getOrganizacion() { return organizacion; }
    public void setOrganizacion(String organizacion) { this.organizacion = organizacion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getPublicoEsperado() { return publicoEsperado; }
    public void setPublicoEsperado(String publicoEsperado) { this.publicoEsperado = publicoEsperado; }
}
