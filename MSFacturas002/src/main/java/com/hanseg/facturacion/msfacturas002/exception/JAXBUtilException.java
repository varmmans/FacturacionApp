package com.hanseg.facturacion.msfacturas002.exception;


public class JAXBUtilException extends Exception {
    
    @SuppressWarnings("compatibility:4056061511682657841")
    private static final long serialVersionUID = 1L;

    public JAXBUtilException(String string, Throwable throwable, boolean b, boolean b1) {
        super(string, throwable, b, b1);
    }

    public JAXBUtilException(Throwable throwable) {
        super(throwable);
    }

    public JAXBUtilException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public JAXBUtilException(String string) {
        super(string);
    }

    public JAXBUtilException() {
        super();
    }
}
