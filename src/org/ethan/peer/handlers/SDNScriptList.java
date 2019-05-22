package org.ethan.peer.handlers;


import org.apache.commons.lang3.StringUtils;
import org.ethan.peer.callbacks.HttpRequests;
import org.ethan.peer.util.PostRequest;

import java.util.HashMap;
import java.util.Map;

public class SDNScriptList {

    public Map<Integer, String> getScriptList() {
        Map<Integer, String> tempMap = new HashMap<>();
        PostRequest http = new PostRequest("https://services.rspeer.org/api/script/list");
        Object postRequest = http.header("Authorization", HttpRequests.getAuthKey());
        Object response = http.asString(postRequest);
        int status = http.getStatus(response);
        System.out.println("Status: " + status);
        String body = (String) http.getBody(response);
        System.out.println(body);
        String[] id = StringUtils.substringsBetween(body, "\"id\":", ",");
        String[] names = StringUtils.substringsBetween(body, "\"name\":\"", "\",");

        for (int i = 0; i < id.length; i++) {
            int intID = Integer.parseInt(id[i]);
            tempMap.put(intID, names[i]);
        }
        return tempMap;
    }

    public void printScriptList() {
        PostRequest http = new PostRequest("https://services.rspeer.org/api/script/list");
        Object postRequest = http.header("Authorization", HttpRequests.getAuthKey());
        Object response = http.asString(postRequest);
        int status = http.getStatus(response);
        System.out.println("Status: " + status);
        String body = (String) http.getBody(response);
        System.out.println(body);
        String[] id = StringUtils.substringsBetween(body, "\"id\":", ",");
        String[] names = StringUtils.substringsBetween(body, "\"name\":\"", "\",");

        for (int i = 0; i < id.length; i++) {
            int intID = Integer.parseInt(id[i]);
            System.out.println("ID: " + intID + " - " + names[i]);
        }
    }

}
