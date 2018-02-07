package com.cbright.sftwitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import org.json.simple.*;

/**
 * Servlet implementation class TwitterFeed
 */
@WebServlet("/TwitterFeed")
public class TwitterFeed extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Consumer Key (API Key)	HYEKLNt51hv8DpgVXr22swalW
	//Consumer Secret (API Secret)	RL76k3hRsnTuntHhSP0HvVMJzigIgzsuYP1ARaGCax0OtAltU9
	//Access Token	960507753499365377-wMOegmpyttGrO5bE20joCCrBjtQ7dtm
	//Access Token Secret	6suqUKQllykuK4XmCfLgOe6uWSGKjMBL2SfS5bGrpvqAq
	//Owner	cbright999
	//Owner ID	960507753499365377
	//Callback URL	https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=salesforce
	//Callback URL Locked	No
	//Sign in with Twitter	Yes
	//App-only authentication	https://api.twitter.com/oauth2/token
	//Request token URL	https://api.twitter.com/oauth/request_token
	//Authorize URL	https://api.twitter.com/oauth/authorize
	//Access token URL	https://api.twitter.com/oauth/access_token

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TwitterFeed() {
        super();
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fstring = request.getParameter("fstring");
		System.out.println("\nTwitterFeed - doGet - fstring = " + fstring);
		
		response.setContentType("application/json;charset=UTF-8"); 
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();
        
		String user = "salesforce";
				
		ConfigurationBuilder cb = new ConfigurationBuilder();        
        
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey("HYEKLNt51hv8DpgVXr22swalW")
        .setOAuthConsumerSecret("RL76k3hRsnTuntHhSP0HvVMJzigIgzsuYP1ARaGCax0OtAltU9")
        .setOAuthAccessToken("960507753499365377-wMOegmpyttGrO5bE20joCCrBjtQ7dtm")
        .setOAuthAccessTokenSecret("6suqUKQllykuK4XmCfLgOe6uWSGKjMBL2SfS5bGrpvqAq");
        
        // Twitter twitter = new TwitterFactory().getInstance();
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        
        //String jsonResponse = "<tweets>\n";
        String jsonResponse = ""; 
        
        JSONObject tweetDetails;
        JSONObject tweetObj;
        JSONObject tweetFinalObj;
        
        JSONArray tweetDetailsArr; 
        JSONArray tweetsArr = new JSONArray(); 
        
        int tweetCount = 0;
        
        try {
        	List<Status> statuses = twitter.getUserTimeline(user);   
        	
        	//response.getWriter().append("Showing @" + user + "'s user timeline.\n\n");
            for (Status status : statuses) {
            	String userName = status.getUser().getName();
            	String screenName = status.getUser().getScreenName();
            	String profileImageURL = status.getUser().getProfileImageURL();
            	String tweetText = status.getText();
            	int retweets = status.getRetweetCount();
            	tweetCount++;
            	
            	if (tweetText.indexOf(fstring)>=0 && tweetCount <= 10) {
	             	tweetDetails = new JSONObject();
	            	tweetDetails.put("userName", userName);
	            	tweetDetails.put("screenName", screenName);
	            	tweetDetails.put("profileImageURL", profileImageURL);
	            	tweetDetails.put("tweetText", tweetText);
	            	tweetDetails.put("retweets", retweets);
	            	
	            	tweetDetailsArr = new JSONArray();
	            	tweetDetailsArr.add(tweetDetails);
	            	
	            	tweetObj = new JSONObject();
	            	tweetObj.put("tweet", tweetDetailsArr);
	            	
	            	tweetsArr.add(tweetObj);
            	}
            	
            	/*
            	jsonResponse += "   <tweet>\n";
            	jsonResponse += "      <userName>" + userName + "</userName>\n";
            	jsonResponse += "      <screenName>" + screenName + "</screenName>\n";
            	jsonResponse += "      <profileImageURL>" + profileImageURL + "</profileImageURL>\n";
            	jsonResponse += "      <tweet>" + tweet + "</tweet>\n";
            	jsonResponse += "      <retweets>" + retweets + "</retweets>\n";
            	jsonResponse += "   </tweet>\n";
            	*/
            }
            
            tweetFinalObj = new JSONObject();
            
            tweetFinalObj.put("tweets", tweetsArr);
            
            //jsonResponse += "</tweets>\n";
            jsonResponse = tweetFinalObj.toString();
            
            System.out.println("Final Json - " + jsonResponse);
            //response.getWriter().append(jsonResponse);
            
            out.print(jsonResponse);
        }
        catch (TwitterException te) {
        	System.out.println("Failed to get timeline: " + te.getMessage() + "\n");
        }
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

