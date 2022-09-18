package com.hanseg.facturacion.msfacturas002.entity;

import java.io.Serializable;

import java.math.BigDecimal;

public class DocumentoPK implements Serializable {

    private BigDecimal codigoDocumento;
    private BigDecimal codigoEmpresa;

    public DocumentoPK() {
    }

    public DocumentoPK(BigDecimal doccoddoc, BigDecimal doccodemp) {
        this.codigoDocumento = doccoddoc;
        this.codigoEmpresa = doccodemp;
    }

    public boolean equals(Object other) {
        if (other instanceof DocumentoPK) {
            final DocumentoPK otherDocumentoPK = (DocumentoPK) other;
            return (otherDocumentoPK == this);
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
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
}
