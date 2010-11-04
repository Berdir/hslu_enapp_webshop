/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import com.sun.jersey.api.client.Client;
import java.util.ArrayList;
import javax.inject.Named;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 *
 * @author berdir
 */
@Named(value="twitterBean")
@ApplicationScoped
public class TwitterBean {
    
    Client client = null;

    List<Status> tweetCache = new ArrayList<Status>();

    Date lastFetched = null;

    /** Creates a new instance of TwitterBean */
    public TwitterBean() {
    }

    public synchronized List<Status> getTweets() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -4);
        Date forFiveMinutes = calendar.getTime();
        if (tweetCache != null && lastFetched != null && lastFetched.after(forFiveMinutes)) {
            return tweetCache;
        }

        Twitter unauthenticatedTwitter = new TwitterFactory().getInstance();

        try {
            tweetCache = unauthenticatedTwitter.getUserTimeline("HSLU_ENAPP");
            return tweetCache;

        } catch (Exception te) {               
            System.out.println(te);
        }
        finally {
            // Also cache in case of an error.
            Calendar newCalendar = Calendar.getInstance();
            lastFetched = newCalendar.getTime();
        }
        return tweetCache;
    }

}
