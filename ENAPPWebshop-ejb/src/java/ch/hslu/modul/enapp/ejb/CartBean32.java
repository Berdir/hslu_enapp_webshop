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
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateful;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.xml.sax.InputSource;

/**
 *
 * @author berdir
 */
@Stateful
public class CartBean32 implements Cart32 {

    @PersistenceContext(name = "webshop-pu")
    EntityManager em;
    @Resource(mappedName = "jms/EnappQueueFactory")
    protected QueueConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/enappqueue")
    protected Queue queue;
    protected List<Product> products = new ArrayList<Product>();

    public CartBean32() {
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

        sendMessage(salesOrder);
        clear();
    }

    @Asynchronous
    protected void sendMessage(SalesOrderJMS mySalesOrderJMS) {

        QueueConnection connection = null;
        try {
            // Set up queue connection.
            connection = connectionFactory.createQueueConnection();
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueSender queueSender = session.createSender(queue);
            queueSender.setDeliveryMode(DeliveryMode.PERSISTENT);

            // Create temporary queue for response.
            TemporaryQueue myReplyToQueue = session.createTemporaryQueue();

            // Create ObjectMessage.
            ObjectMessage objMessage = session.createObjectMessage(mySalesOrderJMS);

            // Set necessary properties of ObjectMessage.
            objMessage.setStringProperty("MessageFormat", "Version 1.0");
            String correlationId = Integer.toString(new Random().nextInt()) + "." + Long.toString(System.currentTimeMillis());
            objMessage.setJMSCorrelationID(correlationId);
            objMessage.setJMSReplyTo(myReplyToQueue);

            // Send message.
            queueSender.send(objMessage);
            //queueSender.close();
            
            // Start connection, this actually sends the message.
            connection.start();

            System.out.print("Sending message, waiting for response... ");

            // Create a consumer and wait a certain time for a response.
            MessageConsumer consumer = session.createConsumer(myReplyToQueue, "JMSCorrelationID = '" + correlationId + "'");
            //consumer.setMessageListener(new MessageListener() {});
            /*Message msgResponse = consumer.receive(10000);

            if (msgResponse != null) {
                System.out.println("OK");
                System.out.println(msgResponse);
            } else {
                System.out.println("Failure");
            }*/

            //System.out.println(msgResponse.getPropertyNames());

        } catch (JMSException ex) {
            Logger.getLogger(CartBean32.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException ex) {
                    Logger.getLogger(CartBean32.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
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
            Logger.getLogger(CartBean32.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CartBean32.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CartBean32.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
