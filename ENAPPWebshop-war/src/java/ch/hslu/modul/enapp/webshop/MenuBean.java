/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author berdir
 */
@Named(value="menu")
@Dependent
public class MenuBean {

    /** Creates a new instance of MenuBean */
    public MenuBean() {
    }

     public List<MenuItem> getMenu(HttpServletRequest request) {
        List<MenuItem> menus = new ArrayList<MenuItem>();

        menus.add(new MenuItem("Home", "Main", request));
        menus.add(new MenuItem("Webshop", "Webshop", request));
        menus.add(new MenuItem("Register", "Register", request));
        menus.add(new MenuItem("Login", "Login", request));
        return menus;
    }

}
