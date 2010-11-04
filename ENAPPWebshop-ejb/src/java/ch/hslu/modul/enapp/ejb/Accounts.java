/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.entity.Purchase;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author berdir
 */
@Local
public interface Accounts {

    ch.hslu.modul.enapp.entity.Customer find(String username, String password);

    List<Purchase> getPurchasedItems(ch.hslu.modul.enapp.entity.Customer customer);

    void register(ch.hslu.modul.enapp.entity.Customer customer);

    void update(ch.hslu.modul.enapp.entity.Customer customer);

}
