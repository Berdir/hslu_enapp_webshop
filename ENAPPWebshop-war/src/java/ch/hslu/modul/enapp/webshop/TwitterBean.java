/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import com.sun.jersey.api.client.Client;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 *
 * @author berdir
 */
@Named(value="twitterBean")
@ApplicationScoped
public class TwitterBean {
    
    Client client = null;

    List<Status> tweetCache = null;

    Date lastFetched = null;

    final String REQUEST_TOKEN_URL = "http://api.twitter.com/oauth/request_token";

    /** Creates a new instance of TwitterBean */
    public TwitterBean() {
    }

    public synchronized List<Status> getTweets() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);
        Date forFiveMinutes = calendar.getTime();
        if (tweetCache != null && lastFetched != null && lastFetched.after(forFiveMinutes)) {
            return tweetCache;
        }

        Twitter unauthenticatedTwitter = new TwitterFactory().getInstance();

        try {
            tweetCache = unauthenticatedTwitter.getUserTimeline("HSLU_ENAPP");
            Calendar newCalendar = Calendar.getInstance();
            lastFetched = newCalendar.getTime();
            return tweetCache;

        } catch (TwitterException te) {
            te.printStackTrace();
            return null;
        }
    }

}
