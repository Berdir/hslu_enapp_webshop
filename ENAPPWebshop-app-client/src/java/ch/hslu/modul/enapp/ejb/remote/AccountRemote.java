/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb.remote;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author berdir
 */
@Remote
public interface AccountRemote {
    List<ICustomer> list();
}
