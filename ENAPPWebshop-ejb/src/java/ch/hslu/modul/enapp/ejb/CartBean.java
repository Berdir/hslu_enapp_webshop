/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.ejb;

import ch.hslu.d3s.enapp.common.SalesOrderJMS;
import ch.hslu.d3s.enapp.common.SalesOrderJMS.PurchaseCustomer;
import ch.hslu.d3s.enapp.common.Util;
import ch.hslu.modul.enapp.entity.Customer;
import ch.hslu.modul.enapp.entity.Product;
import ch.hslu.modul.enapp.entity.Purchase;
import ch.hslu.modul.enapp.entity.Purchaseitem;
import ch.hslu.modul.enapp.lib.CreditCard;
import ch.hslu.modul.enapp.lib.NcResponse;
import ch.hslu.modul.enapp.lib.SHACalculator;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author berdir
 */
@Stateful
public class CartBean implements Cart {

    @PersistenceContext(name = "webshop-pu")
    EntityManager em;

    @Inject
    protected Payment paymentBean;

    protected List<Product> products = new ArrayList<Product>();

    public CartBean() {
        products = new ArrayList<Product>();
    }

    public void add(Product product) {
        if (!products.contains(product)) {
            products.add(product);
        }
    }

    public List<Product> getCart() {
        return products;
    }

    public void remove(Product product) {
        products.remove(product);
    }

    //@Remove
    public void checkout(Customer customer, CreditCard creditCard) {
        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase.setDatetime(Calendar.getInstance().getTime());

        SalesOrderJMS salesOrder = new SalesOrderJMS();
        salesOrder.setPurchaseDate(purchase.getDatetime());

        PurchaseCustomer purchaseCustomer = salesOrder.new PurchaseCustomer(null, customer.getName(), customer.getAddress(), "0000", "Luzern", Integer.toString(customer.getId()), customer.getUsername());
        salesOrder.setPurchaseCustomer(purchaseCustomer);
        salesOrder.setStudent("tagrosse");
        purchase.setStatus("Ordered");

        long totalPrice = 0L;
        List<SalesOrderJMS.PurchaseItem> purchaseItems = new LinkedList<SalesOrderJMS.PurchaseItem>();
        for (Product product : products) {
            Purchaseitem item = new Purchaseitem();
            item.setProduct(product);
            item.setQuantity(1l);
            item.setUnitprice(product.getUnitprice());
            item.setPurchase(purchase);
            purchase.addPurchaseItem(item);

            totalPrice += product.getUnitprice();
            purchaseItems.add(salesOrder.new PurchaseItem(product.getReference(), product.getDescription(), "1", Long.toString(product.getUnitprice())));
        }
        em.persist(purchase);
        // Persist to get id.
        em.flush();

        NcResponse nc = pay( purchase.getId(), totalPrice, creditCard);
        
        salesOrder.setTotalPrice(Long.toString(totalPrice));
        salesOrder.setPurchaseItemList(purchaseItems);
        salesOrder.setPurchaseId(Integer.toString(purchase.getId()));
        salesOrder.setPayId(nc.getPayId());

        String correlationId = paymentBean.sendMessage(salesOrder);

        purchase.setCorrelation(correlationId);
        clear();
    }

    public void clear() {
        products.clear();
    }

    private NcResponse pay(Integer id, long totalPrice, CreditCard creditCard) {

        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("PSPID", Util.PSPID);
        formData.add("OrderId", Integer.toString(id));
        formData.add("USERID", Util.USERID);
        formData.add("PSWD", Util.PSWD);
        formData.add("amount", Long.toString(totalPrice * 100));
        formData.add("currency", "CHF");
        formData.add("CARDNO", creditCard.getCardNo());
        formData.add("ED", creditCard.getExpiryDate());
        formData.add("CN", creditCard.getCustomerName());
        formData.add("CVC", creditCard.getCvc());
        formData.add("BRAND", "visa");
        // String stringToHash = orderID + amount + currency + creditCard + Util.PSPID + Util.SHA1PWDIN;
        String SHA1Source = Integer.toString(id) + Long.toString(totalPrice * 100) + "CHF" + creditCard.getCardNo() + Util.PSPID + Util.SHA1PWDIN;

        System.out.println("SHA 1 Source String : " + SHA1Source);
        System.out.println(formData);

        try {
            formData.add("SHASign", SHACalculator.SHA1(SHA1Source).toUpperCase());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("SHASign: " + formData.get("SHASign").toString());

        System.setProperty("http.proxyHost", "proxy.enterpriselab.ch");
        System.setProperty("http.proxyPort", "8080");

        DefaultApacheHttpClientConfig cc = new DefaultApacheHttpClientConfig();
        cc.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_PROXY_URI,"http://proxy.enterpriselab.ch:8080/");
        ApacheHttpClient client = ApacheHttpClient.create(cc);
        client.setConnectTimeout(10 * 1000);
        WebResource resource = client.resource("https://e-payment.postfinance.ch/ncol/test/orderdirect.asp");
        ClientResponse response = resource.type("application/x-www-form-urlencoded ").post(ClientResponse.class, formData);
        try {
            JAXBContext context = JAXBContext.newInstance(NcResponse.class);
            Unmarshaller u = context.createUnmarshaller();
            NcResponse nc = (NcResponse) u.unmarshal(response.getEntityInputStream());

            System.out.println(nc.toString());
            return nc;
        } catch (JAXBException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
