/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;

/**
 *
 * @author berdir
 */
@Stateful
public class CartBean implements Cart {

    protected List<Product> products = new ArrayList<Product>();
    
    public CartBean() {
        products = new ArrayList<Product>();
    }

    public void addToCart(Product product) {
        products.add(product);
    }

    public List<Product> getCart() {
        return products;
    }
    
 
}
