/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.ejb.Purchases;
import ch.hslu.modul.enapp.entity.Product;
import ch.hslu.modul.enapp.entity.Purchase;
import ch.hslu.modul.enapp.entity.Purchaseitem;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author berdir
 */
@Named(value = "purchases")
@RequestScoped
public class PurchasesMB {

    @Inject
    protected Purchases purchasesEJB;
    @Inject
    protected Login login;

    /** Creates a new instance of Purchases */
    public PurchasesMB() {
    }

    public List<Purchase> getPurchases() {
        List<Purchase> purchases = purchasesEJB.getPurchasedItems(login.getLoggedInCustomer());
        return purchases;
    }

    public String download(Product p) {
        BufferedInputStream input = null;
        try {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setContentType("audio/mp3");
            response.setHeader("Content-Disposition", "attachment; filename=song.mp3");
            URL u = new URL("http://enappsrv01.icompany.intern:8080/data/" + p.getMediapath().replaceAll("\\\\", "/").replaceAll(" ", "%20"));
            input = new BufferedInputStream(u.openStream());
            ServletOutputStream ouputStream = response.getOutputStream();
            int numRead;
            int totalRead = 0;
            byte[] buf = new byte[100];
            while ((numRead = input.read(buf)) >= 0) {
                ouputStream.write(buf, 0, numRead);
                totalRead += numRead;
            }
            response.setContentLength(totalRead);
            ouputStream.flush();
            ouputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PurchasesMB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(PurchasesMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
