/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.entity.Product;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author berdir
 */
@Stateless
public class ProductsBean implements Products {

    @PersistenceContext(name = "webshop-pu")
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

   // @Schedule(minute = "*/10", hour = "*")
    /*
    public void synchronize() {
        List<Item> items = getItems();
        Product product;
        Query q;
        for (Item item : items) {
            q = em.createNamedQuery("Product.findByReference");
            q.setParameter("reference", item.getNo());
            try {
                product = (Product) q.getSingleResult();
                product.setDescription(item.getDescription());
                product.setMediapath(item.getMediafileName());
                product.setUnitprice(item.getUnitPrice().longValue());
                em.merge(product);

            } catch (NoResultException e) {
                // Product does not exist yet, create it.
                product = new Product();
                product.setReference(item.getNo());
                product.setName("bla");
                product.setDescription(item.getDescription());
                product.setMediapath(item.getMediafileName());
                product.setUnitprice(item.getUnitPrice().longValue());
                em.persist(product);
            }
        }
    }

    public List<Item> getItems() {
        try {
            QName itemPageQName = new QName("urn:microsoft-dynamics-schemas/page/item", "Item_Service");
            ItemService itemService;
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
        } catch (Exception ex) {
            Logger.getLogger(ProductsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }*/
}
