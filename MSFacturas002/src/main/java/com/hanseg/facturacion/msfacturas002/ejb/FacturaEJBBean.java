package com.hanseg.facturacion.msfacturas002.ejb;

import autorizacion.ws.sri.gob.ec.Autorizacion;
import autorizacion.ws.sri.gob.ec.RespuestaComprobante;

import com.hanseg.facturacion.msfacturas001.factory.AutorizacionPortFactory;
import com.hanseg.facturacion.msfacturas001.factory.RecepcionPortFactory;
import com.hanseg.facturacion.msfacturas002.entity.Documento;
import com.hanseg.facturacion.msfacturas002.entity.DocumentoPK;
import com.hanseg.facturacion.msfacturas002.enumeration.AmbienteEnum;
import com.hanseg.facturacion.msfacturas002.enumeration.ComprobanteEnum;
import com.hanseg.facturacion.msfacturas002.enumeration.RecepcionEnum;
import com.hanseg.facturacion.msfacturas002.exception.FacturaEJBBeanException;
import com.hanseg.facturacion.msfacturas002.exception.XAdESBESSignatureException;
import com.hanseg.facturacion.msfacturas002.signature.XAdESBESSignature;
import com.hanseg.facturacion.msfacturas002.util.HansegUtil;

import java.io.IOException;

import java.math.BigDecimal;

import java.net.MalformedURLException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.validation.constraints.NotNull;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import recepcion.ws.sri.gob.ec.RespuestaSolicitud;

@Singleton(name = "FacturaEJB", mappedName = "FacturaEJB")
@Local
public class FacturaEJBBean {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "MSFacturas002")
    private EntityManager em;

    private String rutaFirmados = ComprobanteEnum.FIRMADO.getPath();
    private String rutaEnviados = ComprobanteEnum.ENVIADO.getPath();
    private String rutaDevueltos = ComprobanteEnum.DEVUELTO.getPath();
    private String rutaAutorizados = ComprobanteEnum.AUTORIZADO.getPath();
    private String rutaNoAutorizados = ComprobanteEnum.NO_AUTORIZADO.getPath();

    public FacturaEJBBean() {
    }

    //@TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Documento findDocumento(BigDecimal codigoDocumento, BigDecimal codigoEmpresa) {
        Documento documento = em.find(Documento.class, new DocumentoPK(codigoDocumento, codigoEmpresa));
        System.out.println("Documento existe en contexto de persistencia: " + em.contains(documento));
        return documento;
    }

    //@TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Documento mergeDocumento(Documento documento) {
        //System.out.println("Documento existe en contexto de persistencia: " + em.contains(documento));
        return em.merge(documento);
    }

    /** <code>select o from Documento o where o.docstatus = :docstatus</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Documento> getDocumentoFindxStatus(String status) {
        return em.createNamedQuery("Documento.findxStatus", Documento.class)
                 .setParameter("status", status)
                 .getResultList();
    }

    ///////////
    // Business Methods: Metodos de procesamiento de comprobantes basados en flujos que utilizan base de datos
    ///////////

    /**
     * flowComprobantes
     *
     * @throws FacturaEJBBeanException
     */
    //@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void flowComprobantes() throws FacturaEJBBeanException {
        List<Documento> cPendientes = new ArrayList<>();
        // 1.- Leer desde base de datos comprobantes en estado PENDIENTE
        cPendientes = this.getDocumentoFindxStatus(ComprobanteEnum.PENDIENTE.getValue());
        // 2.- Por cada comprobante, Buscar en directorio el archivo de comprobante
        for (Documento cP : cPendientes) {
            System.out.println("callComprobanteTx: " + cP.getNombreArchivoDoc());
            this.callComprobanteTx(cP);
        }
    }

    /**
     * callComprobanteTx
     *
     * @param cP
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void callComprobanteTx(Documento cP) {
        Documento cFirmado;
        RespuestaSolicitud respuesta;
        Path cP_path = Paths.get(cP.getNombreArchivoDoc());
        // 3.- Si existe archivo, Firmar comprobante
        if (Files.exists(cP_path)) {
            try {
                cFirmado = Objects.requireNonNull(this.signComprobante(cP), "No se pudo obtener comprobante firmado.");
                String cF_fileName = cFirmado.getNombreArchivoDoc();
                Date envio = new Date();
                // 4.- Enviar archivo de comprobante firmado al servicio web SRI
                respuesta = this.requestRecepcionSRI(cFirmado);
                // 4.1.- Si el comprobante fue RECIBIDO:
                if (respuesta.getEstado().equals(RecepcionEnum.RECIBIDA.getValue())) {
                    String mensaje = HansegUtil.getComprobanteMessage(respuesta);
                    String status = ComprobanteEnum.ENVIADO.getValue();
                    String cR_fileName = HansegUtil.changePath(cF_fileName, this.rutaEnviados);
                    // 4.1.1.- Mover comprobante a directorio RECIBIDOS
                    Path cF_path = Paths.get(cF_fileName);
                    Path cR_path = Paths.get(cR_fileName);
                    Files.move(cF_path, cR_path, StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(cP_path);
                    // 4.1.2.- Actualizar el estado del comprobante en base de datos a ENVIADO
                    this.updateComprobanteDB(cFirmado, envio, new Date(), mensaje, status, cR_fileName);
                    // 4.2.- Si el comprobante fue DEVUELTO:
                } else if (respuesta.getEstado().equals(RecepcionEnum.DEVUELTA.getValue())) {
                    String mensaje = HansegUtil.getComprobanteMessage(respuesta);
                    String status = ComprobanteEnum.DEVUELTO.getValue();
                    String cD_fileName = HansegUtil.changePath(cF_fileName, this.rutaDevueltos);
                    // 4.2.1.- Copiar comprobante a directorio DEVUELTOS
                    Path cF_path = Paths.get(cF_fileName);
                    Path cD_path = Paths.get(cD_fileName);
                    Files.move(cF_path, cD_path, StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(cP_path);
                    // 4.2.3.- Actualizar el estado del comprobante en base de datos a DEVUELTO
                    this.updateComprobanteDB(cFirmado, envio, new Date(), mensaje, status, cD_fileName);
                }
            } catch (Exception e) {
                System.out.println("Excepción no administrada: " + e.getMessage());
            }
        }
    }

    /**
     * flowAutorizaciones
     *
     * @throws FacturaEJBBeanException
     */
    //@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void flowAutorizaciones() throws FacturaEJBBeanException {
        List<Documento> cEnviados = new ArrayList<>();
        // 1.- Leer desde base de datos comprobantes en estado ENVIADO
        cEnviados = this.getDocumentoFindxStatus(ComprobanteEnum.ENVIADO.getValue());
        // 2.- Por cada comprobante, consultar al servicio web SRI si el comprobante fue autorizado
        Date fechaSolicitudAutorizacionSRI = new Date();
        for (Documento cE : cEnviados) {
            RespuestaComprobante respuesta = this.queryAutorizacionSRI(cE);
            // Por cada autorización en la respuesta llamar a la transacción
            for (Autorizacion a : respuesta.getAutorizaciones().getAutorizacion()) {
                System.out.println("callAutorizacionTx: " + cE.getNombreArchivoDoc());
                this.callAutorizacionTx(cE, a, fechaSolicitudAutorizacionSRI);
            }
        }
    }

    /**
     * callAutorizacionTx
     *
     * @param cE
     * @param a
     * @param fechaSolicitudAutorizacionSRI
     * @throws FacturaEJBBeanException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void callAutorizacionTx(Documento cE, Autorizacion a,
                                    Date fechaSolicitudAutorizacionSRI) throws FacturaEJBBeanException {
        try {
            // 2.1.- Si AUTORIZADO:
            if ("AUTORIZADO".equals(a.getEstado())) {
                // 2.1.1.- Crear archivo comprobante en directorio APROBADOS
                Path cE_path = Paths.get(cE.getNombreArchivoDoc());
                Path cA_path =
                    HansegUtil.createFile(cE_path.getFileName().toString(), this.rutaAutorizados, a.getComprobante());
                // 2.1.2.- Eliminar comprobante de directorio ENVIADOS
                if (Files.deleteIfExists(cE_path)) {
                    // 2.1.3.- Actualizar el estado del comprobante en base de datos a AUTORIZADO
                    Documento c_final = this.findDocumento(cE.getCodigoDocumento(), cE.getCodigoEmpresa());
                    c_final.setFechaSolicitudAutSRI(fechaSolicitudAutorizacionSRI);
                    c_final.setFechaRespuestaAutSRI(HansegUtil.getAutorizacionFecha(a));
                    c_final.setFechaModificaReg(new Date());
                    c_final.setCodigoRespuestaSRI(HansegUtil.getAutorizacionMessage(a));
                    c_final.setStatus(ComprobanteEnum.AUTORIZADO.getValue());
                    c_final.setNombreArchivoDoc(cA_path.getFileName().toString());
                    System.out.println("Documento existe en contexto de persistencia: " + em.contains(c_final));
                    this.mergeDocumento(c_final);
                } else {
                    // Mover el archivo a su ubicación inicial
                    try {
                        Files.move(cA_path, cE_path, StandardCopyOption.REPLACE_EXISTING);
                    } finally {
                        System.out.println("El archivo no se puede eliminar desde su ubicación inicial. Restableciendo!!!");
                    }
                }
                // 2.2.- Si NO AUTORIZADO:
            } else if ("NO AUTORIZADO".equals(a.getEstado())) {
                // 2.2.1.- Crear archivo comprobante en directorio NEGADOS
                Path cE_path = Paths.get(cE.getNombreArchivoDoc());
                Path cNA_path =
                    HansegUtil.createFile(cE_path.getFileName().toString(), this.rutaNoAutorizados, a.getComprobante());
                // 2.2.2.- Eliminar comprobante de directorio ENVIADOS
                if (Files.deleteIfExists(cE_path)) {
                    // 2.2.3.- Actualizar el estado del comprobante en base de datos a NEGADO
                    Documento c_final = this.findDocumento(cE.getCodigoDocumento(), cE.getCodigoEmpresa());
                    c_final.setFechaSolicitudAutSRI(fechaSolicitudAutorizacionSRI);
                    c_final.setFechaModificaReg(new Date());
                    c_final.setCodigoRespuestaSRI(HansegUtil.getAutorizacionMessage(a));
                    c_final.setStatus(ComprobanteEnum.NO_AUTORIZADO.getValue());
                    c_final.setNombreArchivoDoc(cNA_path.getFileName().toString());
                    System.out.println("Documento existe en contexto de persistencia: " + em.contains(c_final));
                    this.mergeDocumento(c_final);
                } else {
                    // Mover el archivo a su ubicación inicial
                    try {
                        Files.move(cNA_path, cE_path, StandardCopyOption.REPLACE_EXISTING);
                    } finally {
                        System.out.println("El archivo no se puede eliminar desde su ubicación inicial. Restableciendo!!!");
                    }
                }
                // 2.3.- Si EN PROCESO:
            } else {
                // 2.3.1.- No hacer nada con el comprobante, para que el proceso se ejecute mas tarde.
                System.out.println("El comprobante tiene estado " + a.getEstado() +
                                   ", se intentará autorizar mas tarde.");
            }
        } catch (Exception e) {
            System.out.println("Excepción no administrada: Monitorear el restablecimiento del archivo a su ubicacion inicial." +
                               e.getMessage());
        }
    }

    /**
     * signComprobante
     *
     * @param comprobante
     * @return
     * @throws FacturaEJBBeanException
     * @throws IOException
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    private Documento signComprobante(@NotNull Documento comprobante) throws FacturaEJBBeanException {
        String fileName = comprobante.getNombreArchivoDoc();
        String certificado = comprobante.getNombreArchivoFirma();
        String clave = comprobante.getClaveFirma();
        String output = HansegUtil.pathless(fileName); //nombre de archivo sin ruta
        String newStatus = ComprobanteEnum.FIRMADO.getValue();
        String newFileName = HansegUtil.changePath(fileName, this.rutaFirmados);

        try {
            Documento signed = null;
            Path p = XAdESBESSignature.firmar(fileName, certificado, clave, this.rutaFirmados, output);
            if (Files.exists(p))
                signed = this.updateComprobanteDB(comprobante, null, null, null, newStatus, newFileName);
            else
                throw new FacturaEJBBeanException("No se pudo crear archivo comprobante firmado" + fileName);
            return signed;
        } catch (XAdESBESSignatureException | IllegalStateException e) {
            throw new FacturaEJBBeanException(e);
        }
    }

    /**
     * sendComprobanteSRI
     *
     * @param comprobante
     * @return
     * @throws FacturaEJBBeanException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private RespuestaSolicitud requestRecepcionSRI(@NotNull Documento comprobante) throws FacturaEJBBeanException {
        RespuestaSolicitud respuesta = null;
        String fileName = comprobante.getNombreArchivoDoc();

        if (comprobante.getStatus().equals(ComprobanteEnum.FIRMADO.getValue())) {
            try {
                Document doc = HansegUtil.getDOMDocument(fileName);
                byte[] data = HansegUtil.getBytesDocument(doc);
                String ambiente, codigo = comprobante.getCodigoAmbiente();
                if (AmbienteEnum.PROD
                                .getCode()
                                .equals(codigo)) {
                    ambiente = AmbienteEnum.PROD.getValue();
                    respuesta = RecepcionPortFactory.getInstance()
                                                    .createRecepcionPort(ambiente)
                                                    .validarComprobante(data);
                }
                if (AmbienteEnum.TEST
                                .getCode()
                                .equals(codigo)) {
                    ambiente = AmbienteEnum.TEST.getValue();
                    respuesta = com.hanseg
                                   .facturacion
                                   .msfacturas000
                                   .factory
                                   .RecepcionPortFactory
                                   .getInstance()
                                   .createRecepcionPort(ambiente)
                                   .validarComprobante(data);
                }
            } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
                throw new FacturaEJBBeanException(e);
            }
        }
        return respuesta;
    }

    /**
     * updateComprobanteDB
     *
     * @param comprobante
     * @param envio
     * @param recepcion
     * @param mensaje
     * @param status
     * @param archivo
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    private Documento updateComprobanteDB(@NotNull Documento comprobante, Date envio, Date recepcion, String mensaje,
                                          String status, String archivo) {
        if (envio != null)
            comprobante.setFechaSolicitudRecSRI(envio);
        if (recepcion != null)
            comprobante.setFechaRespuestaRecSRI(recepcion);
        if (mensaje != null)
            comprobante.setCodigoRespuestaSRI(mensaje);
        comprobante.setStatus(status);
        comprobante.setNombreArchivoDoc(archivo);
        return em.merge(comprobante);
    }

    /**
     * queryAutorizacionSRI
     *
     * @param claveAccesoComprobante
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private RespuestaComprobante queryAutorizacionSRI(@NotNull Documento comprobante) throws FacturaEJBBeanException {
        try {

            String ambiente, codigo = comprobante.getCodigoAmbiente();
            RespuestaComprobante respuesta = null;
            if (AmbienteEnum.PROD
                            .getCode()
                            .equals(codigo)) {
                ambiente = AmbienteEnum.PROD.getValue();
                respuesta = AutorizacionPortFactory.getInstance()
                                                   .createAutorizacionPort(ambiente)
                                                   .autorizacionComprobante(comprobante.getClaveAccesoDoc());
            }
            if (AmbienteEnum.TEST
                            .getCode()
                            .equals(codigo)) {
                ambiente = AmbienteEnum.TEST.getValue();
                respuesta = com.hanseg
                               .facturacion
                               .msfacturas000
                               .factory
                               .AutorizacionPortFactory
                               .getInstance()
                               .createAutorizacionPort(ambiente)
                               .autorizacionComprobante(comprobante.getClaveAccesoDoc());
            }
            return respuesta;
        } catch (MalformedURLException e) {
            throw new FacturaEJBBeanException(e);
        }
    }


}
