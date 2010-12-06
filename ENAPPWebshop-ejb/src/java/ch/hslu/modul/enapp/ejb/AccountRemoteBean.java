/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.ejb.remote.AccountRemote;
import ch.hslu.modul.enapp.ejb.remote.ICustomer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author berdir
 */
@Stateless
public class AccountRemoteBean implements AccountRemote {
    @PersistenceContext(name="webshop-pu")
    private EntityManager em;
    
    @Override
    public List<ICustomer> list() {
        Query q = em.createNamedQuery("Customer.findAll");
        return q.getResultList();
    }
 
}
