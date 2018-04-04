package com.nerisa.datarepo.firebase;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import sun.rmi.runtime.Log;

import javax.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 3/16/18.
 */
public class Notification {

    private static final String FIREBASE_API_KEY = "AAAAmyEe4iY:APA91bFZEllC9CPhVcCRlW3x_bhMF5iDFEoZULJs442chYOyWFhJIsF08Jsb79cWqLP0h64f3kvpAGLrFlOx65fWSMozYgPvE2h6VvmvCygZjP44aZSEIi0oyVuhHJW8LPpVX_CQR8pq";
    private static final String FIREBASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final Logger LOG = Logger.getLogger(Notification.class.getSimpleName());

    private String body;
    private String title;
    private String to;

    public Notification(){}

    public Notification(String title, String body, String to){
        this.title = title;
        this.body = body;
        this.to = to;
    }

    public void sendNotification(){
        JSONObject requestBody = createNotification();
        String jsonData=requestBody.toString();
        HttpPost httpPost=createConnectivity(FIREBASE_URL);
        executeReq(jsonData, httpPost);
    }

    public void sendNotification(JSONObject body){
        LOG.log(Level.INFO, "Sending new warning notification: " + body.toString());
        JSONObject requestBody = createNotification(body);
        String jsonData=requestBody.toString();
        HttpPost httpPost=createConnectivity(FIREBASE_URL);
        executeReq(jsonData, httpPost);
    }

    HttpPost createConnectivity(String restUrl)
    {
        HttpPost post = new HttpPost(restUrl);
        post.setHeader("AUTHORIZATION", "key= " + FIREBASE_API_KEY);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        return post;
    }

    void executeReq(String jsonData, HttpPost httpPost)
    {
        try{
            executeHttpRequest(jsonData, httpPost);
        }
        catch (UnsupportedEncodingException e){
            LOG.log(Level.SEVERE, "error while encoding api url : "+e.getStackTrace());
        }
        catch (IOException e){
            LOG.log(Level.SEVERE, "ioException occured while sending http request : "+e.getStackTrace());
        }
        catch(Exception e){
            LOG.log(Level.SEVERE, "exception occured while sending http request : "+e.getStackTrace());
        }
        finally{
            httpPost.releaseConnection();
        }
    }

    void executeHttpRequest(String jsonData,  HttpPost httpPost)  throws UnsupportedEncodingException, IOException
    {
        HttpResponse response=null;
        String line = "";
        StringBuffer result = new StringBuffer();
        httpPost.setEntity(new StringEntity(jsonData));
        HttpClient client = HttpClientBuilder.create().build();
        response = client.execute(httpPost);
        LOG.log(Level.INFO, "Post parameters : " + jsonData );
        System.out.println(response.toString());
        LOG.log(Level.INFO, "Response Code : " +response.getStatusLine().getStatusCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        while ((line = reader.readLine()) != null){ result.append(line); }
        System.out.println(result.toString());
    }

    private JSONObject createNotification(){
        JSONObject payload = new JSONObject();

        JSONObject notification = new JSONObject();
        notification.put("body", body);
        notification.put("title", title);

        payload.put("to", to);
        payload.put("notification",notification);
        return payload;
    }

    private JSONObject createNotification(JSONObject data){
        JSONObject message = createNotification();
        message.put("data", data);
        return message;
    }
}
