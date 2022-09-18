package com.hanseg.facturacion.msfacturas002.signature;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.Enumeration;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import com.hanseg.facturacion.msfacturas002.exception.GenericXMLSignatureException;

import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.FirmaXML;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class GenericXMLSignature {

    // Ruta de la firma electronica
    private String pathSignature;
    // Clave de la firma electronica
    private String passSignature;
    // Mensaje de error
    private String error;

    /**
     *
     * Ejecución del ejemplo. La ejecución consistiró en la firma de los datos
     * creados por el mótodo abstracto createDataToSign mediante el certificado
     * declarado en la constante PKCS12_FILE. El resultado del proceso de firma será
     * almacenado en un fichero XML en el directorio correspondiente a la constante
     * OUTPUT_DIRECTORY del usuario bajo el nombre devuelto por el mótodo abstracto
     * getSignFileName
     *
     *
     */

    /* Metodos Getters y Setters (Propiedades) */
    public String getPathSignature() {
        return pathSignature;
    }

    public void setPathSignature(String pathSignature) {
        this.pathSignature = pathSignature;
    }

    public String getPassSignature() {
        return passSignature;
    }

    public void setPassSignature(String passSignature) {
        this.passSignature = passSignature;
    }

    protected Path execute() throws GenericXMLSignatureException {
        try {
            // Obtencion del gestor de claves
            KeyStore keyStore = Objects.requireNonNull(getKeyStore(), "No se pudo obtener almacen de firma.");
            String alias = getAlias(keyStore);
            // Obtencion del certificado para firmar. Utilizaremos el primer certificado del almacen.
            X509Certificate certificate =
                Objects.requireNonNull((X509Certificate) keyStore.getCertificate(alias),
                                       "No existe ningún certificado para firmar.");
            // Obtención de la clave privada asociada al certificado
            KeyStore tmpKs = keyStore;
            PrivateKey privateKey = (PrivateKey) tmpKs.getKey(alias, this.passSignature.toCharArray());
            // Obtención del provider encargado de las labores criptográficas
            Provider provider = keyStore.getProvider();
            // Creación del objeto que contiene tanto los datos a firmar como la configuración del tipo de firma
            DataToSign dataToSign = createDataToSign();
            // Creación del objeto encargado de realizar la firma
            FirmaXML firma = new FirmaXML();
            // Firmamos el documento
            Object[] res = firma.signFile(certificate, dataToSign, privateKey, provider);
            Document docSigned = (Document) res[0];
            // Guardamos la firma a un fichero en el home del usuario
            String filePath = getPathOut() + File.separatorChar + getSignatureFileName();
            saveDocumentToDisk(docSigned, filePath);
            System.out.println("Comprobante firmado y guardado en: " + filePath);
            return Paths.get(filePath);
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException e) {
            error = "No existe clave privada para firmar.";
            throw new GenericXMLSignatureException(error, e);
        } catch (KeyStoreException e) {
            error = "No se pudo obtener almacen de firmas o No existe clave privada para firmar.";
            throw new GenericXMLSignatureException(error, e);
        } catch (ClassNotFoundException e) {
            error = "No se encuentra la definición de clase para instanciar un objeto.";
            throw new GenericXMLSignatureException(error, e);
        } catch (NullPointerException e) {
            throw new GenericXMLSignatureException(e);
        } catch (Exception e) {
            error = "Excepción No Administrada: Error al firmar un documento.";
            throw new GenericXMLSignatureException(error, e);
        }
    }

    /**
     *
     * Crea el objeto DataToSign que contiene toda la información de la firma que se
     * desea realizar. Todas las implementaciones deberón proporcionar una
     * implementación de este mótodo
     *
     *
     *
     * @return El objeto DataToSign que contiene toda la información de la firma a
     *         realizar
     */
    protected abstract DataToSign createDataToSign() throws GenericXMLSignatureException;

    protected abstract String getSignatureFileName();

    protected abstract String getPathOut();

    protected Document getDocument(String resource) throws GenericXMLSignatureException {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        File file = new File(resource);
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException e) {
            throw new GenericXMLSignatureException("Error al parsear el documento", e);
        }
        return doc;
    }

    private KeyStore getKeyStore() throws GenericXMLSignatureException {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(pathSignature), passSignature.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new GenericXMLSignatureException(e);
        }
        return ks;
    }

    private static String getAlias(KeyStore keyStore) throws GenericXMLSignatureException {
        String alias = null;
        Enumeration<String> nombres;
        try {
            nombres = keyStore.aliases();
            while (nombres.hasMoreElements()) {
                String tmpAlias = nombres.nextElement();
                if (keyStore.isKeyEntry(tmpAlias))
                    alias = tmpAlias;
            }
        } catch (KeyStoreException e) {
            throw new GenericXMLSignatureException(e);
        }
        return alias;
    }

    public static void saveDocumentToDisk(Document document, String pathXml) throws GenericXMLSignatureException {
        try {
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(pathXml));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new GenericXMLSignatureException(e);
        }
    }
}
