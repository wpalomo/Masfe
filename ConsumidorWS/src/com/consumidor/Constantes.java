package com.consumidor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class Constantes {
   public static final String JDBC_CLASS = "org.hsqldb.jdbcDriver";
   public static final String URL_CONECCION = "jdbc:hsqldb:file:";
   public static final String CLIENTES_TABLE = "clientes";
   public static final String TRANSPORTISTAS_TBLE = "transportistas";
   public static final String EMISOR_TABLE = "emisor";
   public static final String IMPUESTO_TABLE = "impuestos";
   public static final String CLAVES_TABLE = "claves";
   public static final String PRODUCTO_TABLE = "producto";
   public static final String COMPROBANTES_TABLE = "comprobantes";
   public static final String PROXY_TABLE = "PROXY";
   public static final String RESPUESTA_TABLE = "respuesta";
   public static final String RESOURCE_IMPUESTO_VALOR = "resources/impuesto_valor.sql";
   public static final String CONFIGURACION_BD_TABLE = "configuracionBD";
   public static final String CONFIGURACION_DIRECTORIOS_TABLE = "configuracionDirectorio";
   public static final String IMPUESTO_SQL = "resources/impuesto.sql";
   public static final String IMPUESTO_VALOR_SQl = "resources/impuesto_valor.sql";
   public static final String IMPUESTO_VALOR_SQl_WIN = "resources/impuesto_valor_win.sql";
   public static final String COMPROBANTES_SQL = "resources/comprobantes.sql";
   public static final String VERSION = "1.0.0";
   public static final String DIRECTORIO_RECHAZADOS = "rechazados";
   public static final String DIRECTORIO_TRANSMITIDOS = "transmitidosSinRespuesta";
   public static final String XML = ".xml";
   public static final String COD_UTF8 = "UTF-8";
   public static final String ID_FICTICIO_CONSUMIDOR_FINAL = "9999999999999";
   public static final int LIMITE_COMP1 = 3;
   public static final int LIMITE_COMP2 = 9;
   public static final int LIMITE_COMP_MES = 2;
   public static final int LIMITE_COMP_ANIO = 4;
   public static final int LIMITE_CR_NUMDOC = 15;
   public static final int LIMITE_GR_NUMAUT = 37;
   public static final int LIMITE_NUEVA_LINEA = 160;
   public static final int LIMITE_DOC_ADUA = 20;
   public static final int LIMITE_TEXT_AREA = 300;
   public static final String AUTORIDAD_CERT_NO_DISPONIBLE = "61";
   public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
   public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
 
   public static final Integer LONGITUD_CLAVE_CONTINGENCIA = Integer.valueOf(37);
   public static final int INTENTOS_CONEXION_WS = 3;
   public static final int INTENTOS_RESPUESTA_AUTORIZACION_WS = 5;
 
   public static void cargarJDC()
     throws ClassNotFoundException
   {
     Class.forName("org.hsqldb.jdbcDriver");
   }
 
   public static String obtenerUrlBD() {
     Properties props = new Properties();
     String home = System.getProperty("user.home");
     String urlBD = null;
     try {
       props.load(new FileInputStream(home + "/comprobantes.properties"));
       urlBD = props.getProperty("database");
     } catch (FileNotFoundException ex) {
       return null;
     }
     catch (IOException ex) {
       return null;
     }
     return urlBD;
   }
}
