/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.entity.Customer;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author berdir
 */
@Stateless
@LocalBean
public class CustomerSession {
    @PersistenceContext(name="ENAPPWebshop-ejbPU")
    private EntityManager em;

    public void register(Customer customer) {
        
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")


    
    
}
