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

    /**
     *
     * @param salesOrder Mandatory fields should be filled.
     * @return JMS Correlation id
     * @see SalesOrderJMS
     */
    @Override
    public String transmitPurchase(SalesOrderJMS salesOrder) {
        validateSalesOrder(salesOrder);

        QueueConnection connection = null;
        QueueConnection replyConnection = null;
        try {
            // Set up queue connection.
            connection = connectionFactory.createQueueConnection();
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            ObjectMessage objMessage = createJMSMessage(session, salesOrder);

            // Send message.
            QueueSender queueSender = session.createSender(queue);
            queueSender.setDeliveryMode(DeliveryMode.PERSISTENT);
            queueSender.send(objMessage);

            return objMessage.getJMSCorrelationID();

        } catch (JMSException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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

    private MultivaluedMap createFormData(Integer purchaseId, long totalPrice, CreditCard creditCard) {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("PSPID", Util.PSPID);
        formData.add("OrderId", Integer.toString(purchaseId));
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
        String SHA1Source = Integer.toString(purchaseId) + Long.toString(totalPrice * 100) + "CHF" + creditCard.getCardNo() + Util.PSPID + Util.SHA1PWDIN;
        try {
            formData.add("SHASign", SHACalculator.SHA1(SHA1Source).toUpperCase());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formData;
    }

    private void validateSalesOrder(SalesOrderJMS salesOrder) {
        assert salesOrder.getPayId() != null;
        assert salesOrder.getPurchaseId() != null;
    }

    private ObjectMessage createJMSMessage(QueueSession session, SalesOrderJMS salesOrder) throws JMSException {
        ObjectMessage objMessage = session.createObjectMessage(salesOrder);

        // Set necessary properties of ObjectMessage.
        objMessage.setStringProperty("MessageFormat", "Version 1.0");
        String correlationId = Integer.toString(new Random().nextInt()) + "." + Long.toString(System.currentTimeMillis());
        objMessage.setJMSCorrelationID(correlationId);
        objMessage.setJMSReplyTo(replyQueue);
        return objMessage;
    }

    /**
     * Pay purchase with Postfinance.
     *
     * @param purchaseId
     * @param totalPrice Total price in CHF of purchase.
     * @param creditCard
     * @return
     * @throws PaymentResponseException
     */
    @Override
    public String pay(Integer purchaseId, long totalPrice, CreditCard creditCard) throws PaymentResponseException {

        assert creditCard.isValid();
        MultivaluedMap formData = createFormData(purchaseId, totalPrice, creditCard);

        Client client = Client.create();
        client.setConnectTimeout(10 * 1000);
        WebResource resource = client.resource("https://e-payment.postfinance.ch/ncol/test/orderdirect.asp");
        ClientResponse response = resource.type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);
        try {
            JAXBContext context = JAXBContext.newInstance(NcResponse.class);
            Unmarshaller u = context.createUnmarshaller();
            NcResponse nc = (NcResponse) u.unmarshal(response.getEntityInputStream());

            if (Integer.parseInt(nc.getNcError()) > 0) {
                Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, nc.toString());
                throw new PaymentResponseException(nc);
            }
            return nc.getPayId();
        } catch (JAXBException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
