/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.lib;

/**
 *
 * @author berdir
 */
public class PaymentResponseException extends Exception {

    private NcResponse nc;

    public PaymentResponseException(NcResponse nc) {
        this.nc = nc;
    }

    @Override
    public String getMessage() {
        return "Payment Response " + nc.getNcError() + ": " + nc.getNcErrorPlus();
    }



}
