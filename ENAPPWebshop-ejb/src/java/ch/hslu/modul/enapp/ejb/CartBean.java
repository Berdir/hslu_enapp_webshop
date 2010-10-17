/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.entity.Customer;
import ch.hslu.modul.enapp.entity.Product;
import ch.hslu.modul.enapp.entity.Purchase;
import ch.hslu.modul.enapp.entity.Purchaseitem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author berdir
 */
@Stateful
public class CartBean implements Cart {
    @PersistenceContext(name="ENAPPWebshop-ejbPU")
    EntityManager em;

    protected List<Product> products = new ArrayList<Product>();
    
    public CartBean() {
        products = new ArrayList<Product>();
    }

    public void add(Product product) {
        if (!products.contains(product)) {
            products.add(product);
        }
    }

    public List<Product> getCart() {
        return products;
    }

    public void remove(Product product) {
        products.remove(product);
    }

    public void checkout(Customer customer) {
        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase.setDatetime(Calendar.getInstance().getTime());

        for (Product product : products) {
            Purchaseitem item = new Purchaseitem();
            item.setProduct(product);
            item.setQuantity(1l);
            item.setUnitprice(product.getUnitprice());
            item.setPurchase(purchase);
            purchase.addPurchaseItem(item);
        }
        em.persist(purchase);
        clear();

    }

    public void clear()
    {
        products.clear();
    }
    
 
}
