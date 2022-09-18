package com.hanseg.facturacion.msfacturas002.exception;


public class GenericXMLSignatureException extends Exception {

    /**
     * HansegFirmaXMLException
     */
    @SuppressWarnings("compatibility:-3700762017435063190")
    private static final long serialVersionUID = 1L;

    public GenericXMLSignatureException() {
        super("Exception Controlada!!! Error de configuración");
    }

    public GenericXMLSignatureException(String message, Throwable cause, boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GenericXMLSignatureException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericXMLSignatureException(String message) {
        super(message);
    }

    public GenericXMLSignatureException(Throwable cause) {
        super(cause);
    }

}
