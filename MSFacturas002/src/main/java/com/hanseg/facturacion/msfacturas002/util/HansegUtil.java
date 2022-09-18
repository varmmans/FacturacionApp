package com.hanseg.facturacion.msfacturas002.util;

import autorizacion.ws.sri.gob.ec.Autorizacion;
import autorizacion.ws.sri.gob.ec.RespuestaComprobante;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import recepcion.ws.sri.gob.ec.Comprobante;
import recepcion.ws.sri.gob.ec.Mensaje;
import recepcion.ws.sri.gob.ec.RespuestaSolicitud;

public class HansegUtil {

    public HansegUtil() {
        super();
    }

    public static Document getDOMDocument(@NotNull String resource) throws ParserConfigurationException, SAXException,
                                                                           IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        File file = new File(resource);
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(file);
    }

    public static byte[] getBytesDocument(@NotNull Document archivo) throws TransformerConfigurationException,
                                                                            TransformerException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(byteArrayOutputStream);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.newTransformer().transform(new DOMSource(archivo), streamResult);
        return byteArrayOutputStream.toByteArray();
    }

    public static Path createFile(@NotNull String file, @NotNull String directory,
                                          @NotNull String content) throws IOException {
        byte data[] = content.getBytes();
        Path p = Paths.get(directory, file);
        return Files.write(p, data);
    }

    public static String changePath(@NotNull String file, @NotNull String directory) {
        Path f = Paths.get(file);
        return Paths.get(directory, f.getFileName().toString()).toString();
    }

    public static String pathless(String fileName) {
        return Paths.get(fileName)
                    .getFileName()
                    .toString();
    }

    public static String getComprobanteMessage(@NotNull RespuestaSolicitud rs) {
        String str = "-1";
        int i = 0;
        for (Comprobante com : rs.getComprobantes().getComprobante()) {
            List<Mensaje> mensajes = com.getMensajes().getMensaje();
            for (Mensaje msg : mensajes) {
                str = "[ MSG_" + i + " | ";
                str += "Identificador: " + msg.getIdentificador() + " | ";
                str += "Mensaje: " + msg.getMensaje() + " | ";
                str += "Informacion Adicional: " + msg.getInformacionAdicional() + " | ";
                str += "Tipo: " + msg.getTipo() + " ]| ";
                i++;
            }
        }
        return str;
    }

    public static String getAutorizacionMessage(@NotNull RespuestaComprobante rc) {
        String str = "-1";
        int i = 0, j = 0;
        for (Autorizacion aut : rc.getAutorizaciones().getAutorizacion()) {
            str = "AUT_" + i + " | ";
            str += "Estado: " + aut.getEstado() + " | ";
            str += "No: " + aut.getNumeroAutorizacion() + " | ";
            str += "Fecha: " + aut.getFechaAutorizacion() + " | ";
            str += "Ambiente: " + aut.getAmbiente() + " | ";
            i++;
            for (autorizacion.ws.sri.gob.ec.Mensaje msg : aut.getMensajes().getMensaje()) {
                str += "[ MSG_" + j + " | ";
                str += "Identificador: " + msg.getIdentificador() + " | ";
                str += "Info Adicional: " + msg.getInformacionAdicional() + " | ";
                str += "Mensaje: " + msg.getMensaje() + " | ";
                str += "Tipo: " + msg.getTipo() + " ]| ";
                j++;
            }
        }
        return str;
    }

    public static String getAutorizacionMessage(@NotNull Autorizacion aut) {
        String str = "-1";
        int j = 0;

        str += "Estado: " + aut.getEstado() + " | ";
        str += "No: " + aut.getNumeroAutorizacion() + " | ";
        str += "Fecha: " + aut.getFechaAutorizacion() + " | ";
        str += "Ambiente: " + aut.getAmbiente() + " | ";

        for (autorizacion.ws.sri.gob.ec.Mensaje msg : aut.getMensajes().getMensaje()) {
            str += "[ MSG_" + j + " | ";
            str += "Identificador: " + msg.getIdentificador() + " | ";
            str += "Info Adicional: " + msg.getInformacionAdicional() + " | ";
            str += "Mensaje: " + msg.getMensaje() + " | ";
            str += "Tipo: " + msg.getTipo() + " ]| ";
            j++;
        }
        return str;
    }

    public static Date getAutorizacionFecha(@NotNull Autorizacion a) {
        Date date = a.getFechaAutorizacion()
                     .toGregorianCalendar()
                     .getTime();
        return date;
    }
}
