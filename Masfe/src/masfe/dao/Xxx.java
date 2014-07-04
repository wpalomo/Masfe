package masfe.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Xxx {

	 public static String getRutaFirma() {
	        String rutaFirma;
	        Properties prop = new Properties();
	        try {
	            prop.load(new FileInputStream("firma.properties"));
	            rutaFirma = prop.getProperty("ruta_firma");
	        } catch (IOException ex) {
	            rutaFirma = null;
	        }

	        return rutaFirma;

	    }

	 public static String getRutaFirma2() {
	        String rutaFirma;
	        Properties prop = new Properties();
	        try {
	            prop.load(new FileInputStream("firma.properties"));
	            rutaFirma = prop.getProperty("ruta_firma");
	        } catch (IOException ex) {
	            rutaFirma = null;
	        }

	        return rutaFirma;

	    }
	
	public String getData(int value){
		return "Hello";
	}
	
}
