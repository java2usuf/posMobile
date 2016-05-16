package com.ahmed.usuf.billingdesign.utili;

/**
 * Created by myousuff on 5/15/16.
 */
public class HttpClient {
   // static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public String sendMessage(String urlStr, String params){
        /*final HttpURLConnection connection;
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
        return responseString;*/
        return "";
    }
}
