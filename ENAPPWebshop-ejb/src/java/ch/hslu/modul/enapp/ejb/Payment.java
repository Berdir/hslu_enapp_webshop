/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.d3s.enapp.common.SalesOrderJMS;
import javax.ejb.Local;

/**
 *
 * @author berdir
 */
@Local
public interface Payment {

  String sendMessage(SalesOrderJMS mySalesOrderJMS);
    
}
