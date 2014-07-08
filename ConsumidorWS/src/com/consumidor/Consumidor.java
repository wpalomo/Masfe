package com.consumidor;

import java.io.File;




import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;



public class Consumidor {
	
	   //private FileTableModel modelListaArchivos;
	   //private ResultadosTableModel modelResultado = null;
	   //private String claveAcceso = null;
	   private Emisor emisor;
	   Long secuencial;
	   //private String directorioFirmados;
	   //private String PARAM_TIPO_LOTE = "0";
	   //private String VERSION = "1.0.0";
	   //private int MAX_FILE_SIZE = 500;
	  // private int showOnlyOnce = 0;
	   //private int rowEvent = 0;
	
	public void envioIndividual(String RutaArchivo ) throws InterruptedException {
		
		File f = new File(RutaArchivo) ;
		
		emisor =  new Emisor();
		emisor.setTiempoEspera(3);
		emisor.setRuc("0927546143001");
		/*emisor.setNombreComercial();
		emisor.setDirEstablecimiento();
		emisor.setCodigoEstablecimiento();
		emisor.setNumeroResolusion();
		emisor.setContribuyenteEspecial();
		emisor.setCodPuntoEmision();
		Emisor.setClaveInterna();
		emisor.setDireccionMatriz();
		emisor.setToken();
		emisor.setLlevaContabilidad();
		emisor.setPathLogo();*/
		emisor.setTipoEmision("1");
		emisor.setTipoAmbiente("1");
		
		RespuestaSolicitud respuestaRecepcion = new RespuestaSolicitud();
		String respuestaAutoriz = null;
		String claveAccesoComprobante = ArchivoUtils.obtenerValorXML(f, "/*/infoTributaria/claveAcceso");
		String tipoComprobante = ArchivoUtils.obtenerValorXML(f, "/*/infoTributaria/codDoc").substring(1);
		
	 if ((tipoComprobante != null) && (claveAccesoComprobante != null))
       {
		
		respuestaRecepcion = EnvioComprobantesWs.obtenerRespuestaEnvio(f, this.emisor.getRuc(), tipoComprobante, claveAccesoComprobante, FormGenerales.devuelveUrlWs(this.emisor.getTipoAmbiente(), "RecepcionComprobantes"));
       }
		 if (respuestaRecepcion.getEstado().equals("RECIBIDA"))
		 {
			 System.out.println("\n Documento recibido");
			 
			 //Thread.currentThread(); Thread.sleep(this.emisor.getTiempoEspera().intValue() * 1000);
             respuestaAutoriz = AutorizacionComprobantesWs.autorizarComprobanteIndividual(claveAccesoComprobante, f.getName(), this.emisor.getTipoAmbiente());
             	
             System.out.println("\n Documento recibido" + respuestaAutoriz);
             String estado = null;
             String resultado = null;
             if (respuestaAutoriz.lastIndexOf("|") != -1) 
                {
            	 	estado = respuestaAutoriz.substring(0, respuestaAutoriz.lastIndexOf("|"));
            	 	resultado = respuestaAutoriz.substring(respuestaAutoriz.lastIndexOf("|") + 1, respuestaAutoriz.length());
                } else 
                {
                	estado = respuestaAutoriz;
                	resultado = "Procesado";
                 }
  
              if (respuestaAutoriz.equals("AUTORIZADO"))
            	  System.out.println(  "El comprobante fue autorizado por el SRI" + "Respuesta");
              else
              {
            	  System.out.println( "El comprobante fue guardado, firmado y enviado exitósamente, pero no fue Autorizado\n" + estado + "\n" + resultado + "Respuesta");
              }
  
             
  
              f.delete();
		 }
		 else if (respuestaRecepcion.getEstado().equals("DEVUELTA"))
        {
			 System.out.println("Documento DEVUELTA");
		  String resultado = FormGenerales.insertarCaracteres(EnvioComprobantesWs.obtenerMensajeRespuesta(respuestaRecepcion), "\n", 160);
			 
          String dirFirmados = "C:\\firma\\xmlF";
          String dirRechazados = dirFirmados + File.separator + "rechazados";
  
          ArchivoUtils.anadirMotivosRechazo(f, respuestaRecepcion);
  
           File rechazados = new File(dirRechazados);
           if (!rechazados.exists()) {
                new File(dirRechazados).mkdir();
             }
  
           if (!ArchivoUtils.copiarArchivo(f, rechazados.getPath() + File.separator + f.getName()))
        	   System.out.println(  "Error al mover archivo a carpeta rechazados Respuesta");
          else {
            f.delete();
          }
           System.out.println(  "Error al tratar de enviar el comprobante hacia el SRI:\n" + resultado + "Se ha producido un error ");
        }
		
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		String rutaArchivo = "C:\\firma\\xmlF\\0412201301091913450200010020010000000240000005910.xml";
		
		Consumidor Prueba = new Consumidor();
		Prueba.envioIndividual(rutaArchivo);
				
		
	}

}
