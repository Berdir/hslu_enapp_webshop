/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.entity.Product;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.namespace.QName;
import schemas.dynamics.microsoft.page.item.Item;
import schemas.dynamics.microsoft.page.item.ItemFields;
import schemas.dynamics.microsoft.page.item.ItemFilter;
import schemas.dynamics.microsoft.page.item.ItemPort;
import schemas.dynamics.microsoft.page.item.ItemService;

/**
 *
 * @author berdir
 */
@Stateless
public class ProductsBean implements Products {

    @PersistenceContext(name = "ENAPPWebshop-ejbPU")
    private EntityManager em;

    public List<Product> getProducts() {
        Query query = em.createNamedQuery("Product.findAll");
        return query.getResultList();
    }

    private void setAuthenticator(final String domain, final String user, final String password) {
        Authenticator.setDefault(new Authenticator() {

            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(domain + "\\" + user, password.toCharArray());
            }
        });
    }

    public List<Item> getItems() {

        QName itemPageQName = new QName("urn:microsoft-dynamics-schemas/page/item", "Item_Service");
        ItemService itemService;
        try {
            setAuthenticator("ICOMPANY", "icDynNAVWsStudentSvc", "ic0mp@ny");
            URL wsdl = new URL("http://icompanydb01.icompany.intern:7047/DynamicsNAVTest/WS/iCompany%20HSLU%20T&A/Page/Item");

            itemService = new ItemService(wsdl, itemPageQName);

            ItemPort itemPort = itemService.getItemPort();

            List<ItemFilter> filterList = new ArrayList<ItemFilter>();
            ItemFilter filter = new ItemFilter();
            filter.setField(ItemFields.PRODUCT_GROUP_CODE);
            filter.setCriteria("MP3");
            filterList.add(filter);

            return itemPort.readMultiple(filterList, null, 0).getItem();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ProductsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
