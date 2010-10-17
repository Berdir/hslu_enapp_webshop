/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.annotations.LoggedInCustomer;
import ch.hslu.modul.enapp.entity.Customer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author berdir
 */
@Named
@RequestScoped
public class Menu implements Serializable {

    @Inject
    @LoggedInCustomer
    protected Customer customer;

    /** Creates a new instance of MenuBean */
    public Menu() {
    }

    public List<MenuItem> getMenu(HttpServletRequest request) {
        List<MenuItem> menus = new ArrayList<MenuItem>();

        menus.add(new MenuItem("Home", "Main", request));
        menus.add(new MenuItem("Webshop", "Webshop", request));
        if (customer == null) {
            menus.add(new MenuItem("Register", "Register", request));
            menus.add(new MenuItem("Login", "Login", request));
        } else {
            menus.add(new MenuItem("My Account", "Account", request));
            menus.add(new MenuItem("Purchases", "Purchases", request));
            menus.add(new MenuItem("Logout", "Logout", request));
        }

        return menus;
    }
}
