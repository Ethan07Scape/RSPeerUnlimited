package org.ethan.peer.callbacks;

public class HttpRequests {
    private static boolean printHeaders = false;
    private static boolean printRequests = true;
    private static String authKey;
    public static void printPost(String s) {
        if(printRequests)
        System.out.println("Post: "+s);
    }
    public static void printGet(String s) {
        if(printRequests)
        System.out.println("Get: "+s);
    }
    public static void printHeader(String s) {
        if(authKey == null) {
            if(s.contains("bearer")) {
                System.out.println("Set auth key");
                authKey = s;
            }
        }
        if(printHeaders) {
            System.out.println("Header: " + s);
        }
    }

    public static String getAuthKey() {
        return authKey;
    }
}
