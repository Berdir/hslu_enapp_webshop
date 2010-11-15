/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.mdb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author berdir
 */
@MessageDriven(mappedName = "jms/enappreply", activationConfig =  {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    })
public class PaymentReplyBean implements MessageListener {
    
    public PaymentReplyBean() {
    }

    public void onMessage(Message message) {
        try {
            System.out.println("Got response for correlation ID: " + message.getJMSCorrelationID());
        } catch (JMSException ex) {
            Logger.getLogger(PaymentReplyBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
