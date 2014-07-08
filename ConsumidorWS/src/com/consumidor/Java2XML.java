package com.consumidor;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;

public class Java2XML {

	  public static String marshalRespuestaSolicitud(RespuestaSolicitud respuesta, String pathArchivoSalida)
	    {
	      try
	      {
	        JAXBContext context = JAXBContext.newInstance(new Class[] { RespuestaSolicitud.class });
	        Marshaller marshaller = context.createMarshaller();
	        marshaller.setProperty("jaxb.encoding", "UTF-8");
	        marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
	        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(pathArchivoSalida), "UTF-8");
	        marshaller.marshal(respuesta, out);
	      } catch (Exception ex) {
	         System.out.println("error marshalRespuestaSolicitud");
	    	 // Logger.getLogger(Java2XML.class.getName()).log(Level.SEVERE, null, ex);
	        return ex.getMessage();
	      }
	      return null;
	    }
}
