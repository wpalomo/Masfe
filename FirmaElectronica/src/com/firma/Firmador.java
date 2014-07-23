/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.firma;

import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.EnumFormatoFirma;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.javasign.xml.refs.InternObjectToSign;
import es.mityc.javasign.xml.refs.ObjectToSign;
import java.io.IOException;
import org.w3c.dom.Document;

/**
 *
 * @author LDV
 */
public class Firmador extends GenericXMLSignature {

    private String rutaDocumentoAFirmar;
    private String rutaDocumentoFirmado;

    public Firmador(String rutaDocumentoAFirmar, String rutaDocumentoFirmado, String claveFirma) {
        this.rutaDocumentoAFirmar = rutaDocumentoAFirmar;
        this.rutaDocumentoFirmado = rutaDocumentoFirmado;
        PASSWORD = claveFirma;
    }

    @Override
    protected DataToSign createDataToSign() {
        DataToSign dataToSign = new DataToSign();
        try {
            dataToSign.setXadesFormat(EnumFormatoFirma.XAdES_BES);
            dataToSign.setEsquema(XAdESSchemas.XAdES_132);
            dataToSign.setXMLEncoding("UTF-8");
            dataToSign.setEnveloped(true);
            dataToSign.addObject(new ObjectToSign(new InternObjectToSign("comprobante"), "", null, "text/xml", null));
            Document docToSign = getDocument(getRutaDocumentoAFirmar());
            dataToSign.setDocument(docToSign);
        } catch (Exception ex) {
            dataToSign = null;
            System.out.println(ex.getMessage());
        } 
        
        return dataToSign;
        
    }

    @Override
    protected String getSignatureFileName() {
        return rutaDocumentoFirmado;
    }

    public void firmar() {
        execute();
    }

    /**
     * @return the rutaDocumentoAFirmar
     */
    public String getRutaDocumentoAFirmar() {
        return rutaDocumentoAFirmar;
    }

    /**
     * @param rutaDocumentoAFirmar the rutaDocumentoAFirmar to set
     */
    public void setRutaDocumentoAFirmar(String rutaDocumentoAFirmar) {
        this.rutaDocumentoAFirmar = rutaDocumentoAFirmar;
    }

    /**
     * @return the rutaDocumentoFirmado
     */
    public String getRutaDocumentoFirmado() {
        return rutaDocumentoFirmado;
    }

    /**
     * @param rutaDocumentoFirmado the rutaDocumentoFirmado to set
     */
    public void setRutaDocumentoFirmado(String rutaDocumentoFirmado) {
        this.rutaDocumentoFirmado = rutaDocumentoFirmado;
    }

    
    public static void main(String... args) throws IOException {
        //if (args.length == 3) {    
        Firmador prueba = new Firmador("C:\\firma\\xml\\2207201401092754614300110010010000000150000000418.xml", "C:\\firma\\xmlF\\2207201401092754614300110010010000000150000000418.xml", "TH2010mas");
        prueba.firmar();
        System.out.println("TODO OK");
        /* } else {
         System.out.println("<ruta archivo a firmar> <ruta archivo firmado> <clave de archivo .p12>");
         }*/
        //String name = txtRutaArchivoFirmar.getText();
       
    }
}
