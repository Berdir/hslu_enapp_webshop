/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.ejb.Cart;
import ch.hslu.modul.enapp.ejb.Products;
import ch.hslu.modul.enapp.entity.Product;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author berdir
 */
@ManagedBean(name="productList")
@SessionScoped
public class ProductList {
    @EJB
    protected Products productsEJB;

    @EJB
    protected Cart cartEJB;

    /** Creates a new instance of ProductList */
    public List<Product> getProducts() {
        return productsEJB.getProducts();
    }

    public String addToCart(Product product) {

        System.out.println(cartEJB.getCart());
        cartEJB.addToCart(product);
        System.out.println(cartEJB.getCart());
        return "";
    }

    public boolean hasProductsInCart() {
        System.out.println(cartEJB.getCart());
        if (!(cartEJB.getCart().isEmpty())) {
            System.out.println("Not empty!");
        }
        return !cartEJB.getCart().isEmpty();
    }

    public List<Product> getCart() {
        return cartEJB.getCart();
    }

}
