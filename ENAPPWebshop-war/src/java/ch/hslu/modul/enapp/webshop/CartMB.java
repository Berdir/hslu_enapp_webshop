/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.lib.CreditCard;
import ch.hslu.modul.enapp.ejb.Cart;
import ch.hslu.modul.enapp.entity.Product;
import ch.hslu.modul.enapp.lib.PaymentResponseException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    protected CreditCard creditCard;

    /**
     * Get the value of creditCard
     *
     * @return the value of creditCard
     */
    public CreditCard getCreditCard() {
        if (creditCard == null) {
            creditCard = new CreditCard();
        }
        return creditCard;
    }

    /**
     * Set the value of creditCard
     *
     * @param creditCard new value of creditCard
     */
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }


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
        try {
            cartEJB.checkout(login.getLoggedInCustomer(), getCreditCard());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Checkout successful!"));
        } catch (PaymentResponseException ex) {
            Logger.getLogger(CartMB.class.getName()).log(Level.SEVERE, null, ex);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Checkout failed!", "There was an error while processing your purchase!"));

        }

        return "Purchases?faces-redirect=true";
    }

    public String clear() {
        cartEJB.clear();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("All products removed from shopping cart."));
        return redirect();
    }
}
