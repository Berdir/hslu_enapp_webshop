/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import javax.ejb.Local;

/**
 *
 * @author berdir
 */
@Local
public interface Accounts {

    ch.hslu.modul.enapp.entity.Customer find(String username, String password);

    void register(ch.hslu.modul.enapp.entity.Customer customer);

    void update(ch.hslu.modul.enapp.entity.Customer customer);

}
