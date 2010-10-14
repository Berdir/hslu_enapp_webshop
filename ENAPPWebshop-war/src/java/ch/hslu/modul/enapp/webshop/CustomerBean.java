/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.ejb.CustomerSession;
import ch.hslu.modul.enapp.entity.Customer;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author berdir
 */
@ManagedBean(name="customer")
@SessionScoped
public class CustomerBean implements Serializable {

    @EJB
    private CustomerSession customerSession;
    private Customer customer;

        private String repassword;

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    /** Creates a new instance of CustomerBean */
    public CustomerBean() {
        customer = new Customer();
        System.out.println("CustomerBean initalized.");
    }

    public void register() {
        System.out.println("Register called");
        // Verify password.
        //if (    )
        customerSession.register(customer);
        System.out.println(customer);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
