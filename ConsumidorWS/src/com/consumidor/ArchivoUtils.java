package com.consumidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;


public class ArchivoUtils {

	 public static String obtenerValorXML(File xmlDocument, String expression)
	    {
	      String valor = null;
	      try
	      {
	        LectorXPath reader = new LectorXPath(xmlDocument.getPath());
	        valor = (String)reader.leerArchivo(expression, XPathConstants.STRING);
	      }
	      catch (Exception e) {
	    	  System.out.println(" error obtenerValorXML");
	      }
	  
	      return valor;
	    }
	 
	 public static byte[] archivoToByte(File file) throws IOException
    {
      byte[] buffer = new byte[(int)file.length()];
      InputStream ios = null;
      try {
        ios = new FileInputStream(file);
        if (ios.read(buffer) == -1)
          throw new IOException("EOF reached while trying to read the whole file");
      }
      finally {
        try {
          if (ios != null)
            ios.close();
        }
        catch (IOException e) {
          Logger.getLogger(ArchivoUtils.class.getName()).log(Level.SEVERE, null, e);
        }
      }
  
      return buffer;
    }
	 
	    public static File stringToArchivo(String rutaArchivo, String contenidoArchivo)
	    {
	      FileOutputStream fos = null;
	      File archivoCreado = null;
	      try
	      {
	        fos = new FileOutputStream(rutaArchivo);
	        OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
	        for (int i = 0; i < contenidoArchivo.length(); i++) {
	          out.write(contenidoArchivo.charAt(i));
	        }
	        out.close();
	  
	        archivoCreado = new File(rutaArchivo);
	      }
	      catch (Exception ex)
	      {
	         System.out.println(" error stringToArchivo");
	        return null;
	      } finally {
	        try {
	          if (fos != null)
	            fos.close();
	        }
	        catch (Exception ex) {
	        	 System.out.println(" error stringToArchivo");
	        }
	      }
	      return archivoCreado;
	    }
	 
	       public static boolean copiarArchivo(File archivoOrigen, String pathDestino)
	       {
	         FileReader in = null;
	         boolean resultado = false;
	         try
	         {
	           File outputFile = new File(pathDestino);
	           in = new FileReader(archivoOrigen);
	           FileWriter out = new FileWriter(outputFile);
	           int c;
	           while ((c = in.read()) != -1) {
	             out.write(c);
	           }
	           in.close();
	           out.close();
	           resultado = true;
	         }
	         catch (Exception ex) {
	        	 System.out.println(" error copiarArchivo");
	         } finally {
	           try {
	             in.close();
	           } catch (IOException ex) {
	             Logger.getLogger(ArchivoUtils.class.getName()).log(Level.SEVERE, null, ex);
	           }
	         }
	         return resultado;
	       }
	       
      public static boolean adjuntarArchivo(File respuesta, File comprobante)
      {
        boolean exito = false;
        try
        {
          Document document = merge("*", new File[] { comprobante, respuesta });
    
          DOMSource source = new DOMSource(document);
    
          StreamResult result = new StreamResult(new OutputStreamWriter(new FileOutputStream(comprobante), "UTF-8"));
    
          TransformerFactory transFactory = TransformerFactory.newInstance();
          Transformer transformer = transFactory.newTransformer();
    
          transformer.transform(source, result);
        }
        catch (Exception ex) {
        	 System.out.println(" error adjuntarArchivo");
        }
        return exito;
      }
	       
     private static Document merge(String exp, File[] files)
       throws Exception
     {
       XPathFactory xPathFactory = XPathFactory.newInstance();
       XPath xpath = xPathFactory.newXPath();
       XPathExpression expression = xpath.compile(exp);
   
       DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
       docBuilderFactory.setIgnoringElementContentWhitespace(true);
       DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       Document base = docBuilder.parse(files[0]);
   
       Node results = (Node)expression.evaluate(base, XPathConstants.NODE);
       if (results == null) {
         throw new IOException(files[0] + ": expression does not evaluate to node");
       }
   
       for (int i = 1; i < files.length; i++) {
         Document merge = docBuilder.parse(files[i]);
         Node nextResults = (Node)expression.evaluate(merge, XPathConstants.NODE);
         results.appendChild(base.importNode(nextResults, true));
       }
   
       return base;
     }
      
  


      public static boolean anadirMotivosRechazo(File archivo, RespuestaSolicitud respuestaRecepcion)
      {
        boolean exito = false;
        File respuesta = new File("respuesta.xml");
        Java2XML.marshalRespuestaSolicitud(respuestaRecepcion, respuesta.getPath());
        if (adjuntarArchivo(respuesta, archivo) == true) {
          exito = true;
          respuesta.delete();
        }
        return exito;
      }
}
