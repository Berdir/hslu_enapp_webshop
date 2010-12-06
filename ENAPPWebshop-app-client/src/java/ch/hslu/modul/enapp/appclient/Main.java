/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.appclient;

import ch.hslu.modul.enapp.ejb.remote.AccountRemote;
import javax.ejb.EJB;

/**
 *
 * @author berdir
 */
public class Main {
    @EJB
    private static AccountRemote accountRemoteBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(accountRemoteBean.list());
    }

}
