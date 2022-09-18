package com.hanseg.facturacion.msfacturas002.signature;

import org.w3c.dom.Document;

import com.hanseg.facturacion.msfacturas002.exception.GenericXMLSignatureException;
import com.hanseg.facturacion.msfacturas002.exception.XAdESBESSignatureException;

import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.javasign.xml.refs.InternObjectToSign;
import es.mityc.javasign.xml.refs.ObjectToSign;

import java.nio.file.Path;

////Esta es la clase que extiende de Generic.
public class XAdESBESSignature extends GenericXMLSignature {

    private static String nameFile;
    private static String pathFile;

    /**
     * Recurso a firmar
     */
    private String fileToSign;

    /**
     * Fichero donde se desea guardar la firma
     */
    public XAdESBESSignature(String fileToSign) {
        super();
        this.fileToSign = fileToSign;
    }

    /**
     * Punto de entrada al programa
     *
     * @param args
     * Argumentos del programa
     * @throws XAdESBESSignatureException
     */

    public static Path firmar(String xmlPath, String pathSignature, String passSignature, String pathOut,
                              String nameFileOut) throws XAdESBESSignatureException {
        try {
            XAdESBESSignature signature = new XAdESBESSignature(xmlPath);
            signature.setPassSignature(passSignature);
            signature.setPathSignature(pathSignature);
            pathFile = pathOut;
            nameFile = nameFileOut;
            return signature.execute();
        } catch (GenericXMLSignatureException e) {
            throw new XAdESBESSignatureException(e);
        }
    }

    @Override
    protected DataToSign createDataToSign() throws GenericXMLSignatureException {

        DataToSign datosAFirmar = new DataToSign();

        datosAFirmar.setXadesFormat(es.mityc
                                      .javasign
                                      .EnumFormatoFirma
                                      .XAdES_BES);
        datosAFirmar.setEsquema(XAdESSchemas.XAdES_132);
        datosAFirmar.setXMLEncoding("UTF-8");
        datosAFirmar.setEnveloped(true);
        datosAFirmar.addObject(new ObjectToSign(new InternObjectToSign("comprobante"), "contenido comprobante", null,
                                                "text/xml", null));
        datosAFirmar.setParentSignNode("comprobante");

        Document docToSign = getDocument(fileToSign);
        datosAFirmar.setDocument(docToSign);

        return datosAFirmar;
    }

    @Override
    protected String getSignatureFileName() {
        return XAdESBESSignature.nameFile;
    }

    @Override
    protected String getPathOut() {
        return XAdESBESSignature.pathFile;
    }

}
