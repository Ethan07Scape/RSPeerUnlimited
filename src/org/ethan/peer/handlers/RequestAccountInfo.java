package org.ethan.peer.handlers;


import org.ethan.peer.callbacks.HttpRequests;
import org.ethan.peer.util.PostRequest;


public class RequestAccountInfo {

    public String getAccountInfo() {
        PostRequest http = new PostRequest("https://services.rspeer.org/api/user/me?full=true");
        Object postRequest = http.header("Authorization", HttpRequests.getAuthKey());
        Object response = http.asString(postRequest);
        int status = http.getStatus(response);
        System.out.println("Status: "+status);
        String body = (String) http.getBody(response);

        return body;
    }

}
