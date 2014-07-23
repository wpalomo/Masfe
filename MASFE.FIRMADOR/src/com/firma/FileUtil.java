/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.firma;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author LDV
 */
public class FileUtil {

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

}
