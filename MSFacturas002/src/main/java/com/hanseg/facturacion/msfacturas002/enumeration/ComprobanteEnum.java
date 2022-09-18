package com.hanseg.facturacion.msfacturas002.enumeration;

import com.hanseg.facturacion.msfacturas002.util.ResourceUtil;


public enum ComprobanteEnum {
    PENDIENTE("status.pendiente", "ruta.pendientes"),
    FIRMADO("status.firmado", "ruta.firmados"),
    ENVIADO("status.enviado", "ruta.enviados"),
    DEVUELTO("status.devuelto", "ruta.devueltos"),
    AUTORIZADO("status.autorizado", "ruta.autorizados"),
    NO_AUTORIZADO("status.no_autorizado", "ruta.no_autorizados"),
    DESCONOCIDO("status.desconocido", "");

    //
    // Fields
    //
    private final String value;
    private final String path;

    //
    // Constructor
    //
    private ComprobanteEnum(String value, String path) {
        this.value = value;
        this.path = path;
    }

    //
    // Methods
    //
    public String getValue() {
        return ResourceUtil.getResource(value);
    }

    public String getPath() {
        return ResourceUtil.getResource(path);
    }
}
