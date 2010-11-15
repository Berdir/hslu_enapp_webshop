/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.ejb;

import ch.hslu.d3s.enapp.common.SalesOrderJMS;
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
            objMessage.setJMSCorrelationID(correlationId);
            objMessage.setJMSReplyTo(replyQueue);

            // Send message.
            queueSender.send(objMessage);

            return correlationId;

        } catch (JMSException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException ex) {
                    Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
 
}
