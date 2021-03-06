/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import ch.hslu.modul.enapp.ejb.Products;
import ch.hslu.modul.enapp.entity.Product;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author berdir
 */
@Named
@RequestScoped
public class ProductList {
    @Inject
    protected Products productsEJB;

    protected static NumberFormat format = DecimalFormat.getCurrencyInstance(new Locale("de_CH"));

    /** Creates a new instance of ProductList */
    public List<Product> getProducts() {
        return productsEJB.getProducts();
    }

    public static String formatPrice(long price) {
        String formatted = format.format(price);
        // Java doesn't display dashes for 00.
        formatted = formatted.replace("00", "--");
        return formatted;
    }

    static {
        Currency currency = Currency.getInstance("CHF");
	format.setCurrency(currency);
    }
}
