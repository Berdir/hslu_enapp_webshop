/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.d3s.enapp.common.SalesOrderJMS;
import ch.hslu.modul.enapp.lib.CreditCard;
import ch.hslu.modul.enapp.lib.NcResponse;
import ch.hslu.modul.enapp.lib.PaymentResponseException;
import javax.ejb.Local;

/**
 *
 * @author berdir
 */
@Local
public interface Payment {

  String transmitPurchase(SalesOrderJMS mySalesOrderJMS);

  String pay(Integer id, long totalPrice, CreditCard creditCard) throws PaymentResponseException;

}
