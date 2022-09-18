package com.hanseg.facturacion.msfacturas002.exception;


public class FacturaEJBBeanException extends Exception {

    @SuppressWarnings("compatibility:5473849573592522266")
    private static final long serialVersionUID = -3505005993429994211L;

    public FacturaEJBBeanException(String string, Throwable throwable, boolean b, boolean b1) {
        super(string, throwable, b, b1);
    }

    public FacturaEJBBeanException(Throwable throwable) {
        super(throwable);
    }

    public FacturaEJBBeanException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public FacturaEJBBeanException(String string) {
        super(string);
    }

    public FacturaEJBBeanException() {
        super();
    }
}
