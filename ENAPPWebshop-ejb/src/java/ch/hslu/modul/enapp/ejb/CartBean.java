/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.modul.enapp.ejb;

import ch.hslu.d3s.enapp.common.SalesOrderJMS;
import ch.hslu.d3s.enapp.common.SalesOrderJMS.PurchaseCustomer;
import ch.hslu.modul.enapp.entity.Customer;
import ch.hslu.modul.enapp.entity.Product;
import ch.hslu.modul.enapp.entity.Purchase;
import ch.hslu.modul.enapp.entity.Purchaseitem;
import ch.hslu.modul.enapp.lib.CreditCard;
import ch.hslu.modul.enapp.lib.NcResponse;
import ch.hslu.modul.enapp.lib.PaymentResponseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author berdir
 */
@Stateful
public class CartBean implements Cart {

    @PersistenceContext(name = "webshop-pu")
    EntityManager em;

    @Inject
    protected Payment paymentBean;

    protected List<Product> products = new ArrayList<Product>();

    public CartBean() {
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

    public void checkout(Customer customer, CreditCard creditCard) throws PaymentResponseException {

        //EntityTransaction userTransaction = em.getTransaction();

        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase.setDatetime(Calendar.getInstance().getTime());

        SalesOrderJMS salesOrder = new SalesOrderJMS();
        salesOrder.setPurchaseDate(purchase.getDatetime());

        // Empty string is not allowed, either pass real cust id or null.
        String custId = customer.getNavCustId().length() > 0 ? customer.getNavCustId() : null;
        //String custId = null;
        System.out.println("Customer ID: " + custId);
        PurchaseCustomer purchaseCustomer = salesOrder.new PurchaseCustomer(custId, customer.getName(), customer.getAddress(), "0000", "Luzern", Integer.toString(customer.getId()), customer.getUsername());
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

        NcResponse nc;
        try {
            nc = paymentBean.pay(purchase.getId(), totalPrice, creditCard);
        } catch (PaymentResponseException e) {
            //userTransaction.rollback();
            throw e;
        }
        
        salesOrder.setTotalPrice(Long.toString(totalPrice));
        salesOrder.setPurchaseItemList(purchaseItems);
        salesOrder.setPurchaseId(Integer.toString(purchase.getId()));
        salesOrder.setPayId(nc.getPayId());

        String correlationId = paymentBean.sendMessage(salesOrder);

        purchase.setCorrelation(correlationId);
        clear();
        //userTransaction.commit();
    }

    public void clear() {
        products.clear();
    }
}
