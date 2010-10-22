/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.entity.Customer;
import ch.hslu.modul.enapp.entity.Purchase;
import ch.hslu.modul.enapp.lib.SHACalculator;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
        try {
            customer.setPassword(SHACalculator.SHA1(customer.getPassword()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomerSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CustomerSession.class.getName()).log(Level.SEVERE, null, ex);
        }
        em.persist(customer);
    }

    public void update(Customer customer) {
        em.merge(customer);
    }

    public Customer find(String username, String password) {

        Query query = em.createNamedQuery("Customer.findByUsername");
        query.setParameter("username", username);
        for (Customer customer : (List<Customer>)query.getResultList()) {
            try {
                if (customer.getPassword().equals(SHACalculator.SHA1(password))) {
                    return customer;
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(CustomerSession.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(CustomerSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public List<Purchase> getPurchasedItems(Customer customer) {
        Query query = em.createNamedQuery("Purchase.findByUserId");
        query.setParameter("customerid", customer.getId());
        return query.getResultList();
    }
}
