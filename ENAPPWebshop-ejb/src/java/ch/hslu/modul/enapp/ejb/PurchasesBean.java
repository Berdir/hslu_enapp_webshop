/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.ejb;

import ch.hslu.d3s.enapp.common.SalesOrderRestful;
import ch.hslu.modul.enapp.entity.Purchase;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author berdir
 */
@Stateless
public class PurchasesBean implements Purchases {

    @PersistenceContext(name = "webshop-pu")
    EntityManager em;

    @Override
    public SalesOrderRestful loadStatus(String corrId) {
        Client client = Client.create();
        client.setConnectTimeout(10 * 1000);
        WebResource resource = client.resource("http://enappsrv01.icompany.intern:8080/DynNAVdaemon-war/resources/salesorder/" + corrId + "/status");
        System.out.println("REST URL: " + resource.getURI().toString());
        ClientResponse response = resource.type("text/xml").get(ClientResponse.class);

        if (response.getStatus() != 200) {
            Logger.getLogger(CartBean.class.getName()).log(Level.WARNING, "REST returned Status {0}", response.getStatus());
            return null;
        }

        try {
            JAXBContext context = JAXBContext.newInstance(SalesOrderRestful.class);
            Unmarshaller u = context.createUnmarshaller();
            SalesOrderRestful so = (SalesOrderRestful) u.unmarshal(response.getEntityInputStream());

            return so;
        } catch (JAXBException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Purchase> getPurchasedItems(ch.hslu.modul.enapp.entity.Customer customer) {
        Query query = em.createNamedQuery("Purchase.findByUserId");
        query.setParameter("customerid", customer.getId());
        List<Purchase> purchases = query.getResultList();

        // Update status.
        for (Purchase p : purchases) {
            if (p.getCorrelation() != null && p.getStatus().equals("Ordered")) {
                updateStatus(p);
            }
        }

        return purchases;
    }

    @Override
    public boolean updateStatus(Purchase p) {
        SalesOrderRestful so = loadStatus(p.getCorrelation());
        if (so == null) {
            //p.setCorrelation(null);
            return false;
        }
        if (!p.getStatus().equals(so.getStatus())) {
            p.setStatus(so.getStatus());
            return true;
        }
        return false;
    }

    @Override
    public void updateStatus(String corrId) {
        SalesOrderRestful so = loadStatus(corrId);

        if (so == null) {
            return;
        }
        Purchase p = em.find(Purchase.class, so.getPurchaseid());
        p.setStatus(so.getStatus());
    }
}
