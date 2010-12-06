/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.ejb.remote.AccountRemote;
import ch.hslu.modul.enapp.entity.Customer;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author berdir
 */
@Stateless
@RolesAllowed("ADMIN")
public class AccountRemoteBean implements AccountRemote {
    @PersistenceContext(name="webshop-pu")
    private EntityManager em;
    
    @Override
    public List<Customer> list() {
        Query q = em.createNamedQuery("Customer.findAll");
        return q.getResultList();
    }

    @Override
    public void update(Customer customer) {
        em.merge(customer);
    }
 
    
}
