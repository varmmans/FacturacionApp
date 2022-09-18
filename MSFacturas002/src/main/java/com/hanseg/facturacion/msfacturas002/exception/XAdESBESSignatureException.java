package com.hanseg.facturacion.msfacturas002.exception;


public class XAdESBESSignatureException extends Exception {

    /**
     * HansegFirmaXMLException
     */
    @SuppressWarnings("compatibility:620570754771570062")
    private static final long serialVersionUID = 1L;

    public XAdESBESSignatureException() {
        super("Exception Controlada!!! Error de configuración");
    }

    public XAdESBESSignatureException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public XAdESBESSignatureException(String message, Throwable cause) {
        super(message, cause);
    }

    public XAdESBESSignatureException(String message) {
        super(message);
    }

    public XAdESBESSignatureException(Throwable cause) {
        super(cause);
    }

}
