/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author berdir
 */
public class MenuItem {

    protected String name;
    protected HttpServletRequest request;

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }
    protected String path;

    /**
     * Get the value of path
     *
     * @return the value of path
     */
    public String getPath() {
        return request.getContextPath() + "/" + path + ".xhtml";
    }

    /**
     * Set the value of path
     *
     * @param path new value of path
     */
    public void setPath(String path) {
        this.path = path;
    }

    public MenuItem(String name, String path, HttpServletRequest request) {
        this.request = request;
        this.name = name;
        this.path = path;
    }

    public String getCSS() {
        if (isCurrentPath()) {
            return "active";
        }
        return "";
    }

    protected boolean isCurrentPath() {
        return request.getRequestURI().equals(getPath()) || (getPath().contains("Main") && !request.getRequestURI().contains(".xhtml"));
    }
    
}
