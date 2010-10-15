/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.annotations.LoggedInCustomer;
import ch.hslu.modul.enapp.ejb.CustomerSession;
import ch.hslu.modul.enapp.entity.Customer;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author berdir
 */
@Named
@SessionScoped
public class Login implements Serializable{

    @EJB
    CustomerSession customerEJB;

    protected String username;

    protected String password;

    protected Customer loggedInCustomer;

    /** Creates a new instance of LoginBean */
    public Login() {
    }

    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Get the value of passsword
     *
     * @return the value of passsword
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of passsword
     *
     * @param passsword new value of passsword
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Produces
    @LoggedInCustomer
    public Customer getLoggedInCustomer() {
        return loggedInCustomer;
    }

    public void setLoggedInCustomer(Customer loggedInCustomer) {
        this.loggedInCustomer = loggedInCustomer;
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.username, this.password);
            this.loggedInCustomer = customerEJB.find(this.username, this.password);
            return "LOGIN";
        } catch (ServletException e) {
            // Handle unknown username/password in request.login().
            context.addMessage(null, new FacesMessage("Unknown login"));
        }
        return null;
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        reset();

        return "MAIN";
    }

    public boolean loggedIn() {
        return loggedInCustomer != null;
    }

    @PostConstruct
    public void reset() {
        password = null;
        username = null;
        loggedInCustomer = null;
    }

}
