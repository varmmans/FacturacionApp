package com.hanseg.facturacion.msfacturas002.enumeration;


public enum AmbienteEnum {
    TEST("1", "TEST"),
    PROD("2", "PROD");

    //
    // Constructor
    //
    AmbienteEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    //
    // Fields
    //
    private String code;
    private String value;

    //
    // Methods
    //
    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
