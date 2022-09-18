package com.hanseg.facturacion.msfacturas002.util;

import ec.gob.sri.comprobantes.modelo.factura.Factura;

import java.io.File;

import java.net.URI;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Document;

public class JAXBUtil<T> {

    public static void main(String[] args) {
        
        Factura fac = new Factura();
        String xmlFile = "C:\\Facturacion\\0_Pendientes\\PROASSISMED001001000051716.xml";
        
        try {
            fac = JAXBUtil.xml2Object(xmlFile, Factura.class);
            System.out.println(fac.getId());
        } catch (JAXBException jaxbe) {
            // TODO: Add catch code
            jaxbe.printStackTrace();
        } catch (ClassCastException cce) {
            // TODO: Add catch code
            cce.printStackTrace();
        }
    }


    public static <T> T xml2Object(String xmlFile, Class<T> clazz) throws JAXBException, ClassCastException {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller un = context.createUnmarshaller();
            T obj = clazz.cast(un.unmarshal(new File(xmlFile)));
            return obj;
    }
    
    public static <T> T xml2Object(Document document, Class<T> clazz) throws JAXBException, ClassCastException {
            URI uri = URI.create(document.getDocumentURI());
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller un = context.createUnmarshaller();
            T obj = clazz.cast(un.unmarshal(new File(uri)));
            return obj;
    } 


    public static <T> void object2XML(T obj, String xmlFile, Class<T> clazz) throws JAXBException {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Marshaller m = context.createMarshaller();
            //for pretty-print XML in JAXB
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // Write to File
            m.marshal(obj, new File(xmlFile));
    }

}
