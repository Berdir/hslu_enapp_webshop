/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb.remote;

/**
 *
 * @author berdir
 */
public interface ICustomer {

    String getAddress();

    String getEmail();

    Integer getId();

    String getName();

    String getPassword();

    String getUsername();

    void setAddress(String address);

    void setEmail(String email);

    void setId(Integer id);

    void setName(String name);

    void setPassword(String password);

    void setUsername(String username);

}
