/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.mdb;

import ch.hslu.modul.enapp.ejb.Purchases;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

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

    @Inject
    Purchases purchaseBean;

    public PaymentReplyBean() {
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Got response for correlation ID: " + message.getJMSCorrelationID());
            TextMessage tm = (TextMessage)message;
            System.out.println("NAV Order ID: " + tm.getText());

            purchaseBean.loadStatus(message.getJMSCorrelationID());
        } catch (JMSException ex) {
            Logger.getLogger(PaymentReplyBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
