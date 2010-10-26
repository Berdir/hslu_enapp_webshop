/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.ejb.CustomerSession;
import ch.hslu.modul.enapp.entity.Customer;
import ch.hslu.modul.enapp.lib.SHACalculator;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

/**
 *
 * @author berdir
 */
@Named(value = "account")
@SessionScoped
public class Account implements Serializable{

    @Inject
    protected Login login;
    @EJB
    protected CustomerSession customerEJB;

    /** Creates a new instance of Account */
    public Account() {
    }
    protected String newPassword = "";

    /**
     * Get the value of newPassword
     *
     * @return the value of newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    public Customer getCustomer() {
        return login.getLoggedInCustomer();
    }

    public void checkPassword(FacesContext context, UIComponent toValidate, Object value) {
        String confirmPassword = (String) value;
        if (!confirmPassword.equals(getNewPassword())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!", "Passwords do not match!");
            throw new ValidatorException(message);
        }
    }

    /**
     * Set the value of newPassword
     *
     * @param newPassword new value of newPassword
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void update() {
        // If there is a new password, save it.
        if (!newPassword.equals("")) {
            try {
                login.getLoggedInCustomer().setPassword(SHACalculator.SHA1(newPassword));
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        customerEJB.update(login.getLoggedInCustomer());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Account updated."));
    }
}
