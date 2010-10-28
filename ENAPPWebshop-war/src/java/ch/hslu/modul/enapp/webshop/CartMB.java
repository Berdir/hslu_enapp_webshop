/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.ejb.Cart;
import ch.hslu.modul.enapp.entity.Product;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author berdir
 */
@Named("cart")
@SessionScoped
public class CartMB implements Serializable {

    @EJB
    protected Cart cartEJB;

    @Inject
    protected Login login;

    /** Creates a new instance of Cart */
    public CartMB() {
    }

    public String add(Product product) {
        cartEJB.add(product);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(HTMLHelper.stringToHTMLString(product.getDescription()) + " added to shopping cart."));
        return "Webshop?faces-redirect=true";
    }

    public boolean isEmpty() {
        return cartEJB.getCart().isEmpty();
    }

    public String getTotal() {
        long total = 0;
        for (Product p : cartEJB.getCart()) {
            total += p.getUnitprice();
        }

        return ProductList.formatPrice(total);
    }

    public List<Product> getCart() {
        return cartEJB.getCart();
    }

    protected String redirect() {
        return FacesContext.getCurrentInstance().getViewRoot().getViewId() + "?faces-redirect=true";
    }

    public String remove(Product product) {
        cartEJB.remove(product);
        System.out.println(FacesContext.getCurrentInstance().getViewRoot().getViewId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(HTMLHelper.stringToHTMLString(product.getDescription()) + " removed from shopping cart."));
        return redirect();
    }

    public String checkout() {
        cartEJB.checkout(login.getLoggedInCustomer());



        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Checkout successful!"));
        return "Purchases?faces-redirect=true";
    }

    public String clear() {
        cartEJB.clear();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("All products removed from shopping cart."));
        return redirect();
    }
}
