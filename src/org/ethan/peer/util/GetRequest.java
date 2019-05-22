package org.ethan.peer.util;

import java.io.InputStream;
import java.lang.reflect.Method;

public class GetRequest {

    private Object instance;

    public GetRequest(String link) {
        this.instance = getHttpResponse(link);
    }

    public InputStream getRawBody(Object instance) {
        try {
            Class<?> uni = Class.forName("com.mashape.unirest.http.HttpResponse");
            for (Method m : uni.getDeclaredMethods()) {
                if (m.getName().equals("getRawBody")) {
                    return (InputStream) m.invoke(instance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getBody(Object instance) {
        try {
            Class<?> uni = Class.forName("com.mashape.unirest.http.HttpResponse");
            for (Method m : uni.getDeclaredMethods()) {
                if (m.getName().equals("getBody")) {
                    return m.invoke(instance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getStatus(Object instance) {
        try {
            Class<?> uni = Class.forName("com.mashape.unirest.http.HttpResponse");
            for (Method m : uni.getDeclaredMethods()) {
                if (m.getName().equals("getStatus")) {
                    return (int) m.invoke(instance);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Object getHttpResponse(String link) {
        try {
            Class<?> uni = Class.forName("com.mashape.unirest.http.Unirest");
            for (Method m : uni.getDeclaredMethods()) {
                if (m.getName().equals("get")) {
                    return m.invoke(null, link);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getAsBinary(Object instance) {
        try {
            Class<?> uni = Class.forName("com.mashape.unirest.request.GetRequest");
            for (Method m : uni.getMethods()) {
                if (m.getName().equals("asBinary")) {
                    return m.invoke(instance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public Object header(String key, String data) {
        try {
            Class<?> uni = Class.forName("com.mashape.unirest.request.GetRequest");
            for (Method m : uni.getMethods()) {
                if (m.getName().equals("header")) {
                    return m.invoke(instance, key, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
