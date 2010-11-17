/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.mdb;

import ch.hslu.d3s.enapp.common.SalesOrderRestful;
import ch.hslu.modul.enapp.ejb.CartBean;
import ch.hslu.modul.enapp.entity.Purchase;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author berdir
 */
@MessageDriven(mappedName = "jms/enappreply", activationConfig =  {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName= "addressList", propertyValue="mq://10.29.2.50:7676/jms")
    })
public class PaymentReplyBean implements MessageListener {

    @PersistenceContext(name = "webshop-pu")
    EntityManager em;
    
    public PaymentReplyBean() {
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Got response for correlation ID: " + message.getJMSCorrelationID());
            TextMessage tm = (TextMessage)message;
            System.out.println("NAV Order ID: " + tm.getText());

        } catch (JMSException ex) {
            Logger.getLogger(PaymentReplyBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void loadStatus(String corrId) {
        Client client = Client.create();
        client.setConnectTimeout(10 * 1000);
        WebResource resource = client.resource("http://enappsrv01.icompany.intern:8080/DynNAVdaemon-war/resources/salesorder/" + corrId + "/status");
        ClientResponse response = resource.type("text/xml").get(ClientResponse.class);
        try {
            JAXBContext context = JAXBContext.newInstance(SalesOrderRestful.class);
            Unmarshaller u = context.createUnmarshaller();
            SalesOrderRestful so = (SalesOrderRestful) u.unmarshal(response.getEntityInputStream());

            Purchase p = em.find(Purchase.class, so.getPurchaseid());
            p.setStatus(so.getStatus());
        } catch (JAXBException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
