/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.ejb.Accounts;
import ch.hslu.modul.enapp.entity.Customer;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author berdir
 */
@Named
@SessionScoped
public class Register implements Serializable {

    @EJB
    protected Accounts customerSession;
    protected Customer customer;

    @Inject
    protected Login loginMB;

    public void checkPassword(FacesContext context, UIComponent toValidate, Object value) {
        String confirmPassword = (String) value;
        if (!confirmPassword.equals(getCustomer().getPassword())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!", "Passwords do not match!");
            throw new ValidatorException(message);
        }
    }

    /** Creates a new instance of CustomerBean */
    public Register() {
    }

    public String register() {
        customerSession.register(customer);
        loginMB.setLoggedInCustomer(customer);
        return "REGISTER";
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
}
