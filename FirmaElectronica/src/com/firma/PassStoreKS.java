/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.firma;

import es.mityc.javasign.pkstore.IPassStoreKS;
import java.security.cert.X509Certificate;

/**
 *
 * @author LDV
 */
public class PassStoreKS implements IPassStoreKS {

    /** Contraseña de acceso al almacén. */
    private transient String password;

    /**
     * <p>Crea una instancia con la contraseña que se utilizará con el almacén relacionado.</p>
     * @param pass Contraseña del almacén
     */
    public PassStoreKS(final String pass) {
        this.password = pass;
    }

    /**
     * <p>Devuelve la contraseña configurada para este almacén.</p>
     * @param certificate No se utiliza
     * @param alias no se utiliza
     * @return contraseña configurada para este almacén
     * @see es.mityc.javasign.pkstore.IPassStoreKS#getPassword(java.security.cert.X509Certificate, java.lang.String)
     */
    @Override
    public char[] getPassword(final X509Certificate certificate, final String alias) {
        return password.toCharArray();
    }
}
