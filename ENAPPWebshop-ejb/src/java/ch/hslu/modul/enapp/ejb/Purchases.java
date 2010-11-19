/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.d3s.enapp.common.SalesOrderRestful;
import ch.hslu.modul.enapp.entity.Purchase;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author berdir
 */
@Local
public interface Purchases {
    SalesOrderRestful loadStatus(String corrId);
    void updateStatus(String corrId);
    boolean updateStatus(Purchase p);


    List<Purchase> getPurchasedItems(ch.hslu.modul.enapp.entity.Customer customer);
}
