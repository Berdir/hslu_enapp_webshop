/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.modul.enapp.lib.PaymentResponseException;
import ch.hslu.d3s.enapp.common.SalesOrderJMS;
import ch.hslu.d3s.enapp.common.Util;
import ch.hslu.modul.enapp.lib.CreditCard;
import ch.hslu.modul.enapp.lib.NcResponse;
import ch.hslu.modul.enapp.lib.SHACalculator;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author berdir
 */
@Stateless
public class PaymentBean implements Payment {

    @Resource(mappedName = "jms/EnappQueueFactory")
    protected QueueConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/enappqueue")
    protected Queue queue;

    @Resource(mappedName = "jms/EnappQueueReplyFactory")
    protected QueueConnectionFactory replyConnectionFactory;

    @Resource(mappedName = "jms/enappreply")
    protected Queue replyQueue;

    @Override
    public String sendMessage(SalesOrderJMS mySalesOrderJMS) {

        QueueConnection connection = null;
        QueueConnection replyConnection = null;
        try {
            // Set up queue connection.
            connection = connectionFactory.createQueueConnection();
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueSender queueSender = session.createSender(queue);
            queueSender.setDeliveryMode(DeliveryMode.PERSISTENT);

            // Create ObjectMessage.
            ObjectMessage objMessage = session.createObjectMessage(mySalesOrderJMS);

            // Set necessary properties of ObjectMessage.
            objMessage.setStringProperty("MessageFormat", "Version 1.0");
            String correlationId = Integer.toString(new Random().nextInt()) + "." + Long.toString(System.currentTimeMillis());
            System.out.println("Corr ID: " + correlationId);
            objMessage.setJMSCorrelationID(correlationId);

            // Set properties for response.
            System.out.println("ReplyQueue: " + replyQueue.toString());
            objMessage.setJMSReplyTo(replyQueue);

            // Send message.
            System.out.println("Send message");
            queueSender.send(objMessage);

            return correlationId;

        } catch (JMSException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            System.out.println("Finalize");
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException ex) {
                    Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (replyConnection != null) {
                try {
                    replyConnection.close();
                } catch (JMSException ex) {
                    Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    @Override
    public NcResponse pay(Integer id, long totalPrice, CreditCard creditCard) throws PaymentResponseException {

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

        Client client = Client.create();
        client.setConnectTimeout(10 * 1000);
        WebResource resource = client.resource("https://e-payment.postfinance.ch/ncol/test/orderdirect.asp");
        ClientResponse response = resource.type("application/x-www-form-urlencoded ").post(ClientResponse.class, formData);
        try {
            JAXBContext context = JAXBContext.newInstance(NcResponse.class);
            Unmarshaller u = context.createUnmarshaller();
            NcResponse nc = (NcResponse) u.unmarshal(response.getEntityInputStream());

            if (Integer.parseInt(nc.getNcError()) > 0) {
                Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, nc.toString());
                throw new PaymentResponseException(nc);
            }

            System.out.println(nc.toString());
            return nc;
        } catch (JAXBException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
