/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb.remote;

import ch.hslu.modul.enapp.entity.Customer;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author berdir
 */
@Remote
public interface AccountRemote {
    List<Customer> list();

    void update(Customer customer);
}
