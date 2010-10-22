/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.ejb.Products;
import ch.hslu.modul.enapp.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import schemas.dynamics.microsoft.page.item.Item;

/**
 *
 * @author berdir
 */
@ManagedBean(name="productList")
@RequestScoped
public class ProductList {
    @EJB
    protected Products productsEJB;

    /** Creates a new instance of ProductList */
    public List<Product> getProducts() {
        return productsEJB.getProducts();
    }

    public List<Item> getList() {
        return productsEJB.getItems();
    }
}
