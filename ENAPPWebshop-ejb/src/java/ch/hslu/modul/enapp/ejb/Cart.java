/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.entity.Customer;
import ch.hslu.modul.enapp.entity.Product;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author berdir
 */
@Local
public interface Cart {

    void add(Product product);

    List<Product> getCart();

    void remove(Product product);

    public void checkout(Customer customer);

    public void clear();
    
}
