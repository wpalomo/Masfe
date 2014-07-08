package com.consumidor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;


import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.xml.namespace.QName;

import com.thoughtworks.xstream.XStream;

import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.AutorizacionComprobantes;
import ec.gob.sri.comprobantes.ws.aut.AutorizacionComprobantesService;
import ec.gob.sri.comprobantes.ws.aut.Mensaje;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;

public class AutorizacionComprobantesWs {
	   private AutorizacionComprobantesService service;
	   public static final String ESTADO_AUTORIZADO = "AUTORIZADO";
	   public static final String ESTADO_NO_AUTORIZADO = "NO AUTORIZADO";
	 
	   public AutorizacionComprobantesWs(String wsdlLocation)
	   {
	     try
	     {
	       this.service = new AutorizacionComprobantesService(new URL(wsdlLocation), new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesService"));
	     }
	     catch (Exception ex) {
	       //Logger.getLogger(AutorizacionComprobantesWs.class.getName()).log(Level.SEVERE, null, ex);
	       //JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Se ha producido un error ", 0);
	    	 System.out.println("error AutorizacionComprobantesWs");
	     }
	   }
	
	   public static String autorizarComprobanteIndividual(String claveDeAcceso, String nombreArchivo, String tipoAmbiente)
	   {
	     StringBuilder mensaje = new StringBuilder();
	     try {
	       String dirAutorizados = "C:\\firma\\xmlA";
	       String dirNoAutorizados = "C:\\firma\\xmlNA";
	 
	       RespuestaComprobante respuesta = null;
	 
	       for (int i = 0; i < 5; i++) {
	         respuesta = new AutorizacionComprobantesWs(FormGenerales.devuelveUrlWs(tipoAmbiente, "AutorizacionComprobantes")).llamadaWSAutorizacionInd(claveDeAcceso);
	 
	         if (!respuesta.getAutorizaciones().getAutorizacion().isEmpty()) {
	           break;
	         }
	         Thread.currentThread(); Thread.sleep(300L);
	       }
	       int i;
	       if (respuesta != null) {
	         i = 0;
	         for (Autorizacion item : respuesta.getAutorizaciones().getAutorizacion()) {
	           mensaje.append(item.getEstado());
	 
	           item.setComprobante("<![CDATA[" + item.getComprobante() + "]]>");
	 
	           XStream xstream = XStreamUtil.getRespuestaXStream();
	           Writer writer = null;
	           ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	           writer = new OutputStreamWriter(outputStream, "UTF-8");
	           writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	 
	           xstream.toXML(item, writer);
	           String xmlAutorizacion = outputStream.toString("UTF-8");
	 
	           if ((i == 0) && (item.getEstado().equals("AUTORIZADO"))) {
	             ArchivoUtils.stringToArchivo(dirAutorizados + File.separator + nombreArchivo, xmlAutorizacion);
	           //  VisualizacionRideUtil.decodeArchivoBase64(dirAutorizados + File.separator + nombreArchivo, item.getNumeroAutorizacion(), item.getFechaAutorizacion().toString());
	             break;
	           }
	           if (item.getEstado().equals("NO AUTORIZADO")) {
	             ArchivoUtils.stringToArchivo(dirNoAutorizados + File.separator + nombreArchivo, xmlAutorizacion);
	             mensaje.append("|" + obtieneMensajesAutorizacion(item));
	 
	             verificarOCSP(item);
	 
	             break;
	           }
	           i++;
	         }
	       }
	 
	       if ((respuesta == null) || (respuesta.getAutorizaciones().getAutorizacion().isEmpty() == true)) {
	         mensaje.append("TRANSMITIDO SIN RESPUESTA|Ha ocurrido un error en el proceso de la Autorización, por lo que se traslado el archivo a la carpeta de: transmitidosSinRespuesta");
	 
	         String dirFirmados = "C:\\firma\\xmlF";
	         String dirTransmitidos = dirFirmados + File.separator + "TSR";
	 
	         File transmitidos = new File(dirTransmitidos);
	         if (!transmitidos.exists()) {
	           new File(dirTransmitidos).mkdir();
	         }
	 
	         File archivoFirmado = new File(new File(dirFirmados), nombreArchivo);
	         if (!ArchivoUtils.copiarArchivo(archivoFirmado, transmitidos.getPath() + File.separator + nombreArchivo))
	           mensaje.append("\nError al mover archivo a carpeta de Transmitidos sin Respuesta");
	         else
	           archivoFirmado.delete();
	       }
	     }
	     catch (Exception ex)
	     {
	       Logger.getLogger(AutorizacionComprobantesWs.class.getName()).log(Level.SEVERE, null, ex);
	     }
	     return mensaje.toString();
	   }
	   
	      public static String obtieneMensajesAutorizacion(Autorizacion autorizacion)
	      {
	        StringBuilder mensaje = new StringBuilder();
	        for (Mensaje m : autorizacion.getMensajes().getMensaje()) {
	          if (m.getInformacionAdicional() != null)
	            mensaje.append("\n" + m.getMensaje() + ": " + m.getInformacionAdicional());
	          else {
	            mensaje.append("\n" + m.getMensaje());
	          }
	        }
	    
	        return mensaje.toString();
	      }
	      
     public RespuestaComprobante llamadaWSAutorizacionInd(String claveDeAcceso)
     {
       RespuestaComprobante response = null;
       try
       {
         AutorizacionComprobantes port = this.service.getAutorizacionComprobantesPort();
         response = port.autorizacionComprobante(claveDeAcceso);
       }
       catch (Exception e) {
        // Logger.getLogger(AutorizacionComprobantesWs.class.getName()).log(Level.SEVERE, null, e);
    	   System.out.println("error llamadaWSAutorizacionInd");
    	   return response;
       }
   
       return response;
     }
	      
	        public static boolean verificarOCSP(Autorizacion autorizacion)  throws SQLException, ClassNotFoundException
		     {
		       boolean respuesta = true;
		   
		       for (Mensaje m : autorizacion.getMensajes().getMensaje()) {
		         if (m.getIdentificador().equals("61")) {
		           int i = JOptionPane.showConfirmDialog(null, "No se puede validar el certificado digital.\n Desea emitir en contingencia?", "Advertencia", 0);
		           if (i == 0) {
		        //     Emisor emisor = new EmisorSQL().obtenerDatosEmisor();
		         //    FormGenerales.actualizaEmisor(TipoEmisionEnum.CONTINGENCIA.getCode(), emisor);
		           }
		           respuesta = false;
		         }
		       }
		       return respuesta;
		     }
	    		  

}
