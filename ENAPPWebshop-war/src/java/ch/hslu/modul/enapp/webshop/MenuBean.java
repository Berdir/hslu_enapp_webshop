/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author berdir
 */
@ManagedBean(name="menu")
@SessionScoped
public class MenuBean implements Serializable {

    @ManagedProperty(value="#{customer}")
    CustomerBean customer;

    /** Creates a new instance of MenuBean */
    public MenuBean() {
    }

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

     public List<MenuItem> getMenu(HttpServletRequest request) {

        System.out.println("in getmenu");
        System.out.println(customer.getCustomer().getName());

        List<MenuItem> menus = new ArrayList<MenuItem>();

        menus.add(new MenuItem("Home", "Main", request));
        menus.add(new MenuItem("Webshop", "Webshop", request));
        menus.add(new MenuItem("Register", "Register", request));
        menus.add(new MenuItem("Login", "Login", request));
        return menus;
    }

}
