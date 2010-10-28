/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.webshop;

import com.sun.jersey.api.client.Client;
import java.net.URL;
import java.util.ArrayList;
import javax.inject.Named;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import twitter4j.Annotations;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

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
        calendar.add(Calendar.MINUTE, -60);
        Date forFiveMinutes = calendar.getTime();
        if (tweetCache != null && lastFetched != null && lastFetched.after(forFiveMinutes)) {
            return tweetCache;
        }

        Twitter unauthenticatedTwitter = new TwitterFactory().getInstance();

        try {
            tweetCache = unauthenticatedTwitter.getUserTimeline("HSLU_ENAPP");
            return tweetCache;

        } catch (Exception te) {
            // In case of an error, use existing values if possible.
            if (tweetCache.isEmpty()) {
                tweetCache.add(new Status() {

                    public Date getCreatedAt() {
                        return null;
                    }

                    public long getId() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public String getText() {
                        return "Failed to load twitter news.";
                    }

                    public String getSource() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public boolean isTruncated() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public long getInReplyToStatusId() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public int getInReplyToUserId() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public String getInReplyToScreenName() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public GeoLocation getGeoLocation() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public Place getPlace() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public boolean isFavorited() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public User getUser() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public boolean isRetweet() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public Status getRetweetedStatus() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public String[] getContributors() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public long getRetweetCount() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public boolean isRetweetedByMe() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public User[] getUserMentions() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public URL[] getURLs() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public String[] getHashtags() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public Annotations getAnnotations() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public int compareTo(Object o) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public RateLimitStatus getRateLimitStatus() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                });
            }
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
