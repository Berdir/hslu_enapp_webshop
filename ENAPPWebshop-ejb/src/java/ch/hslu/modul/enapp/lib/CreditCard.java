/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.lib;

/**
 *
 * @author berdir
 */
public class CreditCard {
    protected String cardNo;

    /**
     * Get the value of cardNo
     *
     * @return the value of cardNo
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * Set the value of cardNo
     *
     * @param cardNo new value of cardNo
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    protected String cvc;

    /**
     * Get the value of cvc
     *
     * @return the value of cvc
     */
    public String getCvc() {
        return cvc;
    }

    /**
     * Set the value of cvc
     *
     * @param cvc new value of cvc
     */
    public void setCvc(String cvc) {
        this.cvc = cvc;
    }


    protected String expiryDate;

    /**
     * Get the value of expiryDate
     *
     * @return the value of expiryDate
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Set the value of expiryDate
     *
     * @param expiryDate new value of expiryDate
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }


    protected String customerName;

    /**
     * Get the value of customerName
     *
     * @return the value of customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Set the value of customerName
     *
     * @param customerName new value of customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
