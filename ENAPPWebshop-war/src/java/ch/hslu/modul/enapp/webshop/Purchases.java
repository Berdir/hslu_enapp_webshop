/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.ejb.CustomerSession;
import ch.hslu.modul.enapp.entity.Purchase;
import ch.hslu.modul.enapp.entity.Purchaseitem;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author berdir
 */
@Named(value = "purchases")
@RequestScoped
public class Purchases {

    @EJB
    protected CustomerSession customerEJB;
    @Inject
    protected Login login;

    /** Creates a new instance of Purchases */
    public Purchases() {
    }

    public List<Purchase> getPurchases() {
        List<Purchase> purchases = customerEJB.getPurchasedItems(login.getLoggedInCustomer());
        System.out.println(purchases);
        return purchases;
    }

    public String extractSongs(Purchase purchase) {
        String songs = "";
        int i = 0;
        for (Purchaseitem item : purchase.getPurchaseitemCollection()) {
            if (i == 0) {
                songs += item.getProduct().getDescription();
            } else {
                songs += ", " + item.getProduct().getDescription();
            }
            i++;
        }

        return songs;
    }
}
