/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.firma;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.mityc.firmaJava.libreria.utilidades.UtilidadTratarNodo;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.javasign.pkstore.CertStoreException;
import es.mityc.javasign.pkstore.IPKStoreManager;
import es.mityc.javasign.pkstore.keystore.KSStore;
import java.security.AuthProvider;


import java.util.Enumeration;
/**
 *
 * @author JVA
 */
public abstract class GenericXMLSignature {

    /**
     * <p>
     * Directorio donde se almacenará el resultado de la firma
     * </p>
     */
    public final static String OUTPUT_DIRECTORY = "";
    public static String PASSWORD = "";
    public AuthProvider aprov;
     

    /**
     * <p>
     * Ejecución del ejemplo. La ejecución consistirá en la firma de los datos
     * creados por el método abstracto
     * <code>createDataToSign</code> mediante el certificado declarado en la
     * constante
     * <code>PKCS12_FILE</code>. El resultado del proceso de firma será
     * almacenado en un fichero XML en el directorio correspondiente a la
     * constante
     * <code>OUTPUT_DIRECTORY</code> del usuario bajo el nombre devuelto por el
     * método abstracto
     * <code>getSignFileName</code>
     * </p>
     */
    protected void execute() {

        // Obtencion del gestor de claves
        IPKStoreManager storeManager = getPKStoreManager();
        if (storeManager == null) {
            System.out.println("E01NO ALMACEN CLAVES");
            System.exit(-1);
        }

        // Obtencion del certificado para firmar. Utilizaremos el primer
        // certificado del almacen.
        X509Certificate certificate = getFirstCertificate(storeManager);
        if (certificate == null) {
            System.out.println("E02SIN CERTIFICADO");
            System.exit(-1);
        }

        // Obtención de la clave privada asociada al certificado
        PrivateKey privateKey;
        try {
            privateKey = storeManager.getPrivateKey(certificate);
        } catch (CertStoreException e) {
            System.out.println("E03CLAVE INCORRECTA");
            return;
        }

        // Obtención del provider encargado de las labores criptográficas
        Provider provider = storeManager.getProvider(certificate);

        /*
         * Creación del objeto que contiene tanto los datos a firmar como la
         * configuración del tipo de firma
         */
        DataToSign dataToSign = createDataToSign();

        // Firmamos el documento
        Document docSigned = null;
        try {
            /*
             * Creación del objeto encargado de realizar la firma
             */
            FirmaXML firma = createFirmaXML();
            Object[] res = firma.signFile(certificate, dataToSign, privateKey, provider);
            docSigned = (Document) res[0];
        } catch (Exception ex) {
            System.out.println("E04NO EXISTE OBJETO FIRMA");
            System.exit(-1);
        }

        // Guardamos la firma a un fichero en el home del usuario
//        String filePath = OUTPUT_DIRECTORY + File.separatorChar + getSignatureFileName();
       // String filePath = getSignatureFileName();
        saveDocumentToFile(docSigned, getSignatureFileName());
    }

    /**
     * <p>
     * Crea el objeto DataToSign que contiene toda la información de la firma
     * que se desea realizar. Todas las implementaciones deberán proporcionar
     * una implementación de este método
     * </p>
     *
     * @return El objeto DataToSign que contiene toda la información de la firma
     * a realizar
     */
    protected abstract DataToSign createDataToSign();

    /**
     * <p>
     * Nombre del fichero donde se desea guardar la firma generada. Todas las
     * implementaciones deberán proporcionar este nombre.
     * </p>
     *
     * @return El nombre donde se desea guardar la firma generada
     */
    protected abstract String getSignatureFileName();

    /**
     * <p>
     * Crea el objeto
     * <code>FirmaXML</code> con las configuraciones necesarias que se encargará
     * de realizar la firma del documento.
     * </p>
     * <p>
     * En el caso más simple no es necesaria ninguna configuración específica.
     * En otros casos podría ser necesario por lo que las implementaciones
     * concretas de las diferentes firmas deberían sobreescribir este método
     * (por ejemplo para añadir una autoridad de sello de tiempo en aquellas
     * firmas en las que sea necesario)
     * <p>
     *
     *
     * @return firmaXML Objeto <code>FirmaXML</code> configurado listo para
     * usarse
     */
    protected FirmaXML createFirmaXML() {
        return new FirmaXML();
    }

    /**
     * <p>
     * Escribe el documento a un fichero.
     * </p>
     *
     * @param document El documento a imprmir
     * @param pathfile El path del fichero donde se quiere escribir.
     */
    private void saveDocumentToFile(Document document, String pathfile) {
        try {
            FileOutputStream fos = new FileOutputStream(pathfile);
            UtilidadTratarNodo.saveDocumentToOutputStream(document, fos, true);
        } catch (FileNotFoundException ex) {
            System.out.println("E05PATH XML FIRMADO ERRONEO");
            System.exit(-1);
        }
    }

    /**
     * <p>
     * Escribe el documento a un fichero. Esta implementacion es insegura ya que
     * dependiendo del gestor de transformadas el contenido podría ser alterado,
     * con lo que el XML escrito no sería correcto desde el punto de vista de
     * validez de la firma.
     * </p>
     *
     * @param document El documento a imprmir
     * @param pathfile El path del fichero donde se quiere escribir.
     */
    @SuppressWarnings("unused")
    private void saveDocumentToFileUnsafeMode(Document document, String pathfile) {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        try {
            serializer = tfactory.newTransformer();

            serializer.transform(new DOMSource(document), new StreamResult(new File(pathfile)));
        } catch (TransformerException ex) {
            System.out.println("E05PATH XML FIRMADO ERRONEO");
            System.exit(-1);
        }
    }

    /**
     * <p>
     * Devuelve el
     * <code>Document</code> correspondiente al
     * <code>resource</code> pasado como parámetro
     * </p>
     *
     * @param resource El recurso que se desea obtener
     * @return El <code>Document</code> asociado al <code>resource</code>
     */
    protected Document getDocument(String resource) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try {
//            doc = dbf.newDocumentBuilder().parse(this.getClass().getResourceAsStream(resource));
            doc = dbf.newDocumentBuilder().parse(new File(resource));
        } catch (ParserConfigurationException ex) {
            System.out.println("E06PATH XML NO FIRMADO ERRONEO");
            System.exit(-1);
        } catch (SAXException ex) {
            System.out.println("E06PATH XML NO FIRMADO ERRONEO");
            System.exit(-1);
        } catch (IOException ex) {
            System.out.println("E06PATH XML NO FIRMADO ERRONEO");
            System.exit(-1);
        } catch (IllegalArgumentException ex) {
            System.out.println("E06PATH XML NO FIRMADO ERRONEO");
            System.exit(-1);
        }
        return doc;
    }

    /**
     * <p>
     * Devuelve el contenido del documento XML correspondiente al
     * <code>resource</code> pasado como parámetro
     * </p> como un
     * <code>String</code>
     *
     * @param resource El recurso que se desea obtener
     * @return El contenido del documento XML como un <code>String</code>
     */
    protected String getDocumentAsString(String resource) {
        Document doc = getDocument(resource);
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        StringWriter stringWriter = new StringWriter();
        try {
            serializer = tfactory.newTransformer();
            serializer.transform(new DOMSource(doc), new StreamResult(stringWriter));
        } catch (TransformerException ex) {
            System.out.println("E06PATH XML NO FIRMADO ERRONEO");
            System.exit(-1);
        }

        return stringWriter.toString();
    }

    /**
     * <p>
     * Devuelve el gestor de claves que se va a utilizar
     * </p>
     *
     * @return El gestor de claves que se va a utilizar</p>
     */
    private IPKStoreManager getPKStoreManager() {

        IPKStoreManager storeManager = null;
        try {

            KeyStore ks = KeyStore.getInstance("PKCS12");
            //KeyStore ks = KeyStore.getInstance("X.509");
            //KeyStore ks = KeyStore.getInstance("JKS");
            //KeyStore ks = KeyStore.getInstance("Windows-MY");
            //KeyStore ks = KeyStore.getInstance("PKCS12");
           // System.out.println( PASSWORD.toCharArray());
            ks.load(new java.io.FileInputStream(Constantes.getRutaFirma()), PASSWORD.toCharArray());

            storeManager = new KSStore(ks, new PassStoreKS(PASSWORD));
            
 
    
            for (Enumeration<?> e = ks.aliases(); e.hasMoreElements();) {
                String alias = (String) e.nextElement();
                System.out.println("@:" + alias);
            }
        } catch (KeyStoreException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } catch (CertificateException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } catch (IOException ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            System.exit(-1);
        }
        return storeManager;
    }

    /**
     * <p>
     * Recupera el primero de los certificados del almacén.
     * </p>
     *
     * @param storeManager Interfaz de acceso al almacén
     * @return Primer certificado disponible en el almacén
     */
    private X509Certificate getFirstCertificate(final IPKStoreManager storeManager) {
//        IPKStoreManager storeManager = MSCAPICertificateStoreListing.obtenerAlamacenCertificados();
//        storeManager.
        List<X509Certificate> certs = null;
        try {
            certs = storeManager.getSignCertificates();
        } catch (CertStoreException ex) {
            System.out.println("E09CERTIFICADO NO DISPONIBLE");
            System.exit(-1);
        }
        if ((certs == null) || (certs.isEmpty())) {
            System.out.println("E10CERTIFICADO VACIO");
            System.exit(-1);
        }

        X509Certificate certificate = certs.get(0);
        return certificate;
    }
}
