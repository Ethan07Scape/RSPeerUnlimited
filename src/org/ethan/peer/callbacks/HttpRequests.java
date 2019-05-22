package org.ethan.peer.callbacks;

import org.apache.commons.lang3.StringUtils;

public class HttpRequests {
    private static boolean printHeaders = false;
    private static boolean printRequests = true;
    private static String authKey;
    private static String clientID;

    public static void printPost(String s) {
        if (printRequests) {
            System.out.println("Post: " + s);
        }
        String id = StringUtils.substringBetween(s, "clientId=", "&");
        if (id != null && id.length() > 0) {
            System.out.println("Found: " + id);
            setClientID(id);
        }
    }

    public static void printGet(String s) {
        if (printRequests)
            System.out.println("Get: " + s);
    }

    public static void printHeader(String s) {
        if (authKey == null) {
            if (s.contains("bearer")) {
                System.out.println("Set auth key");
                authKey = s;
            }
        }
        if (printHeaders) {
            System.out.println("Header: " + s);
        }
    }

    public static String getAuthKey() {
        return authKey;
    }

    public static String getClientID() {
        return clientID;
    }

    public static void setClientID(String clientID) {
        HttpRequests.clientID = clientID;
    }

    private boolean test() {
        if (true) {
            return true;
        }
        return false;
    }
}
