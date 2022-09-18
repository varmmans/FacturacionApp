package com.hanseg.facturacion.msfacturas002.exception;


public class FacturacionElectronicaEJBBeanException extends Exception {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -3505005993429994211L;

    public FacturacionElectronicaEJBBeanException(String string, Throwable throwable, boolean b, boolean b1) {
        super(string, throwable, b, b1);
    }

    public FacturacionElectronicaEJBBeanException(Throwable throwable) {
        super(throwable);
    }

    public FacturacionElectronicaEJBBeanException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public FacturacionElectronicaEJBBeanException(String string) {
        super(string);
    }

    public FacturacionElectronicaEJBBeanException() {
        super();
    }
}
