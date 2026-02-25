package com.servired.servired.model;

import jakarta.persistence.*;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Profesional {
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String profesion;
    private String telefono;
    private String email;
    private String estado;

    private String descripcion;
    private String zona;
    private Double precioBase;

    private Boolean activo;
    private String tipoSuscripcion;
    private String fechaAlta;

    public Profesional()  {
        this.activo = true;
    }
    public String getZona() {return zona;}

    public void setZona(String zona) {this.zona = zona;}

    public String getDescripcion() {return descripcion;}

    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public Long getId() {
        return id;
    }

    public String getEstado() {
        return estado;}
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public void setEstado(String estado) {
        this.estado = estado;}

    public void setId(Long id) {this.id = id;}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
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
}