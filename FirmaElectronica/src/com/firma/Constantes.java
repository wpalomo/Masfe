/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.firma;

/**
 *
 * @author LDV
 */
public class Constantes {

    public static String PKCS12_RESOURCE;
    public static String PKCS12_PASSWORD;

    public static String getRutaFirma() {
        if (PKCS12_RESOURCE == null || PKCS12_RESOURCE.isEmpty()) {
            PKCS12_RESOURCE = FileUtil.getRutaFirma();
        }
        return PKCS12_RESOURCE;
    }
}
