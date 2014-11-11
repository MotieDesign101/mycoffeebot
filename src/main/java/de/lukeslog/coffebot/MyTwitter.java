package de.lukeslog.coffebot;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lukas on 04.07.14.
 */
public class MyTwitter {

    private static String OAUTH_CONSUMER_KEY= OAuthData.OAUTH_CONSUMER_KEY; //not in the repository for obvious reasons
    private static String OAUTH_CONSUMER_SECRET =OAuthData.OAUTH_CONSUMER_SECRET;//not in the repository for obvious reasons
    private static String OAUTH_ACCESS_TOKEN = OAuthData.OAUTH_ACCESS_TOKEN;//not in the repository for obvious reasons
    private static String OAUTH_ACCESS_TOKEN_SECRET = OAuthData.OAUTH_ACCESS_TOKEN_SECRET;//not in the repository for obvious reasons

    public static void tweet(String text)
    {

        if(!(text.equals("")))
        {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey(OAUTH_CONSUMER_KEY);
            cb.setOAuthConsumerSecret(OAUTH_CONSUMER_SECRET);
            cb.setOAuthAccessToken(OAUTH_ACCESS_TOKEN);
            cb.setOAuthAccessTokenSecret(OAUTH_ACCESS_TOKEN_SECRET);
            if (OAUTH_ACCESS_TOKEN.equals("")) {
                try {
                    login();
                } catch (TwitterException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //Abfrage starten
            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
            try
            {
                Status status = twitter.updateStatus(text);
                System.out.println("tweet "+text);
                System.out.println("Successfully updated the status to ["+ status.getText() +" ].");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void login() throws TwitterException, IOException {
        // The factory instance is re-useable and thread safe.
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(OAUTH_CONSUMER_KEY, OAUTH_CONSUMER_SECRET);
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            String pin = br.readLine();
            try{
                if(pin.length() > 0){
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                }else{
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if(401 == te.getStatusCode()){
                    System.out.println("Unable to get the access token.");
                }else{
                    te.printStackTrace();
                }
            }
        }
        //persist to the accessToken for future reference.
        storeAccessToken(twitter.verifyCredentials().getId() , accessToken);
    }
    private static void storeAccessToken(long useId, AccessToken accessToken){
        System.out.println(accessToken.getToken());
        System.out.println(accessToken.getTokenSecret());
    }
}
