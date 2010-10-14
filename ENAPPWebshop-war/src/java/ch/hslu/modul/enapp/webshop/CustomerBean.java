/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.ejb.CustomerSession;
import ch.hslu.modul.enapp.entity.Customer;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author berdir
 */
@ManagedBean(name = "customer")
@SessionScoped
public class CustomerBean implements Serializable {

    @EJB
    private CustomerSession customerSession;
    private Customer customer;

    public void checkPassword(FacesContext context, UIComponent toValidate, Object value) {
        String confirmPassword = (String) value;
        if (!confirmPassword.equals(getCustomer().getPassword())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!", "Passwords do not match!");
            throw new ValidatorException(message);
        }
    }

    /** Creates a new instance of CustomerBean */
    public CustomerBean() {

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
        if (customer == null) {
            customer = new Customer();
        }
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String login() {
        Customer loggedInCustomer = customerSession.login(customer.getUsername(), customer.getPassword());
        if (loggedInCustomer != null) {
            System.out.println("Log in successfull!");
            customer = loggedInCustomer;
        }
        return null;
    }
}
