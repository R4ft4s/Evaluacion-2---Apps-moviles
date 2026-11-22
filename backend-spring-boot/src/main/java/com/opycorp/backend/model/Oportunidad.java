package com.opycorp.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "oportunidades")
public class Oportunidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipo;
    private String descripcion;
    private String fuente;
    private String prioridad;
    private String potencial;
    private String estado;
    private String observaciones;
    private String ubicacion;
    private String fotoPath;
    private String fechaDeteccion;
    private String organizacion;
    private String contactoNombre;
    private String contactoTelefono;
    private String publicoEsperado;
    private String responsable;
    private String condicionesEspacio;
    private String condicionesCosto;
    private String condicionesRequisitos;
    private String resultadoActivacion;
    private String impactoEvaluacion;
    private String proximaAccion;

    public Oportunidad() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFuente() { return fuente; }
    public void setFuente(String fuente) { this.fuente = fuente; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getPotencial() { return potencial; }
    public void setPotencial(String potencial) { this.potencial = potencial; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getFotoPath() { return fotoPath; }
    public void setFotoPath(String fotoPath) { this.fotoPath = fotoPath; }

    public String getFechaDeteccion() { return fechaDeteccion; }
    public void setFechaDeteccion(String fechaDeteccion) { this.fechaDeteccion = fechaDeteccion; }

    public String getOrganizacion() { return organizacion; }
    public void setOrganizacion(String organizacion) { this.organizacion = organizacion; }

    public String getContactoNombre() { return contactoNombre; }
    public void setContactoNombre(String contactoNombre) { this.contactoNombre = contactoNombre; }

    public String getContactoTelefono() { return contactoTelefono; }
    public void setContactoTelefono(String contactoTelefono) { this.contactoTelefono = contactoTelefono; }

    public String getPublicoEsperado() { return publicoEsperado; }
    public void setPublicoEsperado(String publicoEsperado) { this.publicoEsperado = publicoEsperado; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }

    public String getCondicionesEspacio() { return condicionesEspacio; }
    public void setCondicionesEspacio(String condicionesEspacio) { this.condicionesEspacio = condicionesEspacio; }

    public String getCondicionesCosto() { return condicionesCosto; }
    public void setCondicionesCosto(String condicionesCosto) { this.condicionesCosto = condicionesCosto; }

    public String getCondicionesRequisitos() { return condicionesRequisitos; }
    public void setCondicionesRequisitos(String condicionesRequisitos) { this.condicionesRequisitos = condicionesRequisitos; }

    public String getResultadoActivacion() { return resultadoActivacion; }
    public void setResultadoActivacion(String resultadoActivacion) { this.resultadoActivacion = resultadoActivacion; }

    public String getImpactoEvaluacion() { return impactoEvaluacion; }
    public void setImpactoEvaluacion(String impactoEvaluacion) { this.impactoEvaluacion = impactoEvaluacion; }

    public String getProximaAccion() { return proximaAccion; }
    public void setProximaAccion(String proximaAccion) { this.proximaAccion = proximaAccion; }
}
