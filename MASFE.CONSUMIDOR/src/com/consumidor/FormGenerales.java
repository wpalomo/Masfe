package com.consumidor;


import java.net.MalformedURLException;


public class FormGenerales {

	public static String devuelveUrlWs(String ambiente, String nombreServicio)
	   {
	     StringBuilder url = new StringBuilder();
	     String direccionIPServicio = null;
	     Proxy configuracion = null;
	     try
	     {
	    	
	    	//String url, Integer puerto, String usuario, String clave, String wsProduccion, String wsPruebas 
	       configuracion = new Proxy();
	       configuracion.setWsProduccion("https://celcer.sri.gob.ec");
	       configuracion.setWsPruebas("https://celcer.sri.gob.ec");
	     } catch (Exception ex) {
	       System.out.println("Proxy mal configurado");
	     }
	     
	     if (configuracion != null)
	     {
	       if (configuracion.getUrl() != null) {
	         String uri = configuracion.getUrl() + ":" + configuracion.getPuerto();
	         ProxyConfig.init(uri);
	       }
	 
	       if (ambiente.equals(TipoAmbienteEnum.PRODUCCION.getCode()) == true)
	         direccionIPServicio = configuracion.getWsProduccion();
	       else if (ambiente.equals(TipoAmbienteEnum.PRUEBAS.getCode()) == true) {
	         direccionIPServicio = configuracion.getWsPruebas();
	       }
	       url.append(direccionIPServicio);
	 
	       url.append("/comprobantes-electronicos-ws/");
	       url.append(nombreServicio);
	       url.append("?wsdl");
	       System.out.print(url.toString());
	     }
	     return url.toString();
	   }
	 
	   public static String validarUrl(String tipoAmbiente, String nombreServicio)
	   {
	     String mensaje = null;
	     String url = devuelveUrlWs(tipoAmbiente, nombreServicio);
	     Object c = EnvioComprobantesWs.webService(url);
	     if ((c instanceof MalformedURLException)) {
	       mensaje = ((MalformedURLException)c).getMessage();
	     }
	     return mensaje;
	   }
	   
	      public static String insertarCaracteres(String cadenaLarga, String aInsertar, int longitud)
	      {
	        StringBuilder sb = new StringBuilder(cadenaLarga);
	    
	        int i = 0;
	        while ((i = sb.indexOf(" ", i + longitud)) != -1) {
	          sb.replace(i, i + 1, aInsertar);
	        }
	    
	        return sb.toString();
	      }
	
}
