package com.hanseg.facturacion.msfacturas002.enumeration;


public enum RecepcionEnum {
    RECIBIDA("RECIBIDA"),
    DEVUELTA("DEVUELTA"),
    DESCONOCIDO("");
    
    //
    // Constructor
    //
    RecepcionEnum(String value) {
        this.value = value;
    }

    //
    // Fields
    //
    private String value;

    //
    // Methods
    //
    public String getValue() {
        return value;
    }
    
}
