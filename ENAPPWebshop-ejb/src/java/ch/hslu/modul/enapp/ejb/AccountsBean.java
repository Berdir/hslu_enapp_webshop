/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.entity.CustomerGroup;
import ch.hslu.modul.enapp.entity.CustomerGroupPK;
import ch.hslu.modul.enapp.entity.Purchase;
import ch.hslu.modul.enapp.lib.SHACalculator;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author berdir
 */
@Stateless
public class AccountsBean implements Accounts {
    @PersistenceContext(name="webshop-pu")
    private EntityManager em;

    public void register(ch.hslu.modul.enapp.entity.Customer customer) {
        try {
            customer.setPassword(SHACalculator.SHA1(customer.getPassword()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AccountsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AccountsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        em.persist(customer);
        CustomerGroup group = new CustomerGroup();
        group.setCustomerGroupPK(new CustomerGroupPK("USER", customer.getUsername()));
        em.persist(group);
    }

    public void update(ch.hslu.modul.enapp.entity.Customer customer) {
        em.merge(customer);
    }

    public ch.hslu.modul.enapp.entity.Customer find(String username, String password) {

        Query query = em.createNamedQuery("Customer.findByUsername");
        query.setParameter("username", username);
        for (ch.hslu.modul.enapp.entity.Customer customer : (List<ch.hslu.modul.enapp.entity.Customer>)query.getResultList()) {
            try {
                if (customer.getPassword().equals(SHACalculator.SHA1(password))) {
                    return customer;
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(AccountsBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(AccountsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public List<Purchase> getPurchasedItems(ch.hslu.modul.enapp.entity.Customer customer) {
        Query query = em.createNamedQuery("Purchase.findByUserId");
        query.setParameter("customerid", customer.getId());
        return query.getResultList();
    }
}
