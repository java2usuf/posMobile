package com.ahmed.usuf.billingdesign.utili;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.base.Throwables;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by myousuff on 5/15/16.
 */
public class HttpClient {
    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public String sendMessage(String urlStr, String params){
        final HttpURLConnection connection;
        String responseString ="";
        try {

            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
            GenericUrl url = new GenericUrl(urlStr);
            HttpContent content = new ByteArrayContent("application/x-www-form-urlencoded", params.getBytes());
            HttpRequest request = requestFactory.buildPostRequest(url, content);
            HttpResponse response = request.execute();
            System.out.println("Sent parameters: " + "" + " - Received response: " + response.getStatusMessage());
            responseString = CharStreams.toString(new InputStreamReader(response.getContent()));
            System.out.println("Response content: " + responseString);
        } catch (IOException e) {
            //throw Throwables.propagate(e);
        }
        return responseString;
    }
}
