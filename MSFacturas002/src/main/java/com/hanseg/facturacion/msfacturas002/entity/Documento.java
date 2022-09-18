package com.hanseg.facturacion.msfacturas002.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({ @NamedQuery(name = "Documento.findAll", query = "select o from Documento o"),
                @NamedQuery(name = "Documento.findxStatus",
                            query = "select o from Documento o where o.status = :status")
    })
@Table(name = "DOCUMENTOEL")
@IdClass(DocumentoPK.class)
public class Documento implements Serializable {

    @Id
    @Column(nullable = false, name = "doccoddoc")
    private BigDecimal codigoDocumento;
    @Id
    @Column(nullable = false, name = "doccodemp")
    private BigDecimal codigoEmpresa;
    @Column(nullable = false, length = 4000, name = "docarchiv")
    private String nombreArchivoDoc;
    @Column(nullable = false, length = 500, name = "docclavea")
    private String claveAccesoDoc;
    @Column(nullable = false, length = 4000, name = "docclavef")
    private String claveFirma;
    @Column(nullable = false, name = "doccodrel")
    private BigDecimal codigoInterno;
    @Column(length = 500, name = "doccodres")
    private String codigoRespuestaSRI;
    @Column(nullable = false, length = 50, name = "doccoduse")
    private String codigoUsuario;
    @Temporal(TemporalType.DATE)
    @Column(name = "docfecenv")
    private Date fechaSolicitudRecSRI;
    @Temporal(TemporalType.DATE)
    @Column(name = "docfecrep")
    private Date fechaRespuestaRecSRI;
    @Temporal(TemporalType.DATE)
    @Column(name = "docfecend")
    private Date fechaSolicitudAutSRI;
    @Temporal(TemporalType.DATE)
    @Column(name = "docfecapr")
    private Date fechaRespuestaAutSRI;
    @Temporal(TemporalType.DATE)
    @Column(name = "docfechac")
    private Date fechaEnvioCliente;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, name = "docfecmod")
    private Date fechaModificaReg;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, name = "docfecnew")
    private Date fechaCreacionReg;
    @Column(nullable = false, length = 4000, name = "docfirmae")
    private String nombreArchivoFirma;
    @Column(nullable = false, length = 1, name = "docstatua")
    private String statusAutorizado;
    @Column(nullable = false, length = 1, name = "docstatuc")
    private String statusCliente;
    @Column(nullable = false, length = 1, name = "docstatue")
    private String statusEnvio;
    @Column(nullable = false, length = 1, name = "docstatur")
    private String statusRecibido;
    @Column(length = 20, name = "docstatus")
    private String status;
    @Column(nullable = false, length = 2, name = "doctipdoc")
    private String tipoDocumento;
    @Column(nullable = false, length = 50, name = "docusemod")
    private String usuarioModificaReg;
    @Column(nullable = false, length = 50, name = "docusenew")
    private String usuarioCreacionReg;
    @Column(nullable = false, length = 1, name = "doccodamb")
    private String codigoAmbiente;

    public Documento() {
    }

    @NotNull
    public String getNombreArchivoDoc() {
        return nombreArchivoDoc;
    }

    public void setNombreArchivoDoc(String docarchiv) {
        this.nombreArchivoDoc = docarchiv;
    }
    
    @NotNull
    public String getClaveAccesoDoc() {
        return claveAccesoDoc;
    }

    public void setClaveAccesoDoc(String docclavea) {
        this.claveAccesoDoc = docclavea;
    }

    public String getClaveFirma() {
        return claveFirma;
    }

    public void setClaveFirma(String docclavef) {
        this.claveFirma = docclavef;
    }

    public BigDecimal getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(BigDecimal doccoddoc) {
        this.codigoDocumento = doccoddoc;
    }

    public BigDecimal getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(BigDecimal doccodemp) {
        this.codigoEmpresa = doccodemp;
    }

    public BigDecimal getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(BigDecimal doccodrel) {
        this.codigoInterno = doccodrel;
    }

    public String getCodigoRespuestaSRI() {
        return codigoRespuestaSRI;
    }

    public void setCodigoRespuestaSRI(String doccodres) {
        this.codigoRespuestaSRI = doccodres;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String doccoduse) {
        this.codigoUsuario = doccoduse;
    }

    public Date getFechaRespuestaAutSRI() {
        return fechaRespuestaAutSRI;
    }

    public void setFechaRespuestaAutSRI(Date docfecapr) {
        this.fechaRespuestaAutSRI = docfecapr;
    }

    public Date getFechaSolicitudAutSRI() {
        return fechaSolicitudAutSRI;
    }

    public void setFechaSolicitudAutSRI(Date docfecend) {
        this.fechaSolicitudAutSRI = docfecend;
    }

    public Date getFechaSolicitudRecSRI() {
        return fechaSolicitudRecSRI;
    }

    public void setFechaSolicitudRecSRI(Date docfecenv) {
        this.fechaSolicitudRecSRI = docfecenv;
    }

    public Date getFechaEnvioCliente() {
        return fechaEnvioCliente;
    }

    public void setFechaEnvioCliente(Date docfechac) {
        this.fechaEnvioCliente = docfechac;
    }

    public Date getFechaModificaReg() {
        return fechaModificaReg;
    }

    public void setFechaModificaReg(Date docfecmod) {
        this.fechaModificaReg = docfecmod;
    }

    public Date getFechaCreacionReg() {
        return fechaCreacionReg;
    }

    public void setFechaCreacionReg(Date docfecnew) {
        this.fechaCreacionReg = docfecnew;
    }

    public Date getFechaRespuestaRecSRI() {
        return fechaRespuestaRecSRI;
    }

    public void setFechaRespuestaRecSRI(Date docfecrep) {
        this.fechaRespuestaRecSRI = docfecrep;
    }

    public String getNombreArchivoFirma() {
        return nombreArchivoFirma;
    }

    public void setNombreArchivoFirma(String docfirmae) {
        this.nombreArchivoFirma = docfirmae;
    }

    public String getStatusAutorizado() {
        return statusAutorizado;
    }

    public void setStatusAutorizado(String docstatua) {
        this.statusAutorizado = docstatua;
    }

    public String getStatusCliente() {
        return statusCliente;
    }

    public void setStatusCliente(String docstatuc) {
        this.statusCliente = docstatuc;
    }

    public String getStatusEnvio() {
        return statusEnvio;
    }

    public void setStatusEnvio(String docstatue) {
        this.statusEnvio = docstatue;
    }

    public String getStatusRecibido() {
        return statusRecibido;
    }

    public void setStatusRecibido(String docstatur) {
        this.statusRecibido = docstatur;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String docstatus) {
        this.status = docstatus;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String doctipdoc) {
        this.tipoDocumento = doctipdoc;
    }

    public String getUsuarioModificaReg() {
        return usuarioModificaReg;
    }

    public void setUsuarioModificaReg(String docusemod) {
        this.usuarioModificaReg = docusemod;
    }

    public String getUsuarioCreacionReg() {
        return usuarioCreacionReg;
    }

    public void setUsuarioCreacionReg(String docusenew) {
        this.usuarioCreacionReg = docusenew;
    }

    public void setCodigoAmbiente(String codigoAmbiente) {
        this.codigoAmbiente = codigoAmbiente;
    }

    public String getCodigoAmbiente() {
        return codigoAmbiente;
    }
}
