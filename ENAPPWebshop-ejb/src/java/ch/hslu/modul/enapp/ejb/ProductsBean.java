/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.entity.Product;
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
public class ProductsBean implements Products {

    @PersistenceContext(name = "ENAPPWebshop-ejbPU")
    private EntityManager em;

    public List<Product> getProducts() {
        Query query = em.createNamedQuery("Product.findAll");
        return query.getResultList();
    }
}
