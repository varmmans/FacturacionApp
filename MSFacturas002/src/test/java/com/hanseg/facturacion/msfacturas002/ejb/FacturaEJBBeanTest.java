package com.hanseg.facturacion.msfacturas002.ejb;

import com.hanseg.facturacion.msfacturas002.entity.Documento;
import com.hanseg.facturacion.msfacturas002.enumeration.EstadoComprobante;
import com.hanseg.facturacion.msfacturas002.exception.FacturaEJBBeanException;

import java.util.List;

import javax.ejb.EJB;

import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class FacturaEJBBeanTest {

    @EJB
    private FacturaEJBBean facturacionEJB;

    public FacturaEJBBeanTest() {
        //facturacionEJB = new FacturaEJBBean();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * @see FacturaEJBBean#flowComprobantes()
     */
    @Test
    public void testFlowComprobantes() {
        try {
            facturacionEJB.flowComprobantes();
        } catch (FacturaEJBBeanException e) {
            fail(e.getMessage());
        }
    }

    /**
     * @see com.hanseg.facturacion.ejb.FacturaEJBBean#getDocumentoFindAll()
     */
    @Test
    public void testGetDocumentoFindAll() {
        List<Documento> docs = facturacionEJB.getDocumentoFindAll();
        for (Documento o : docs) {
            System.out.println(o.getDocarchiv());
        }
    }

    /**
     * @see FacturaEJBBean#getComprobantesPendientes()
     */
    @Test
    public void testGetComprobantesPendientes() {
        List<Documento> docs = facturacionEJB.getDocumentoxStatus(EstadoComprobante.PENDIENTE);
        for (Documento o : docs) {
            System.out.println(o.getDocarchiv());
        }
    }
}
