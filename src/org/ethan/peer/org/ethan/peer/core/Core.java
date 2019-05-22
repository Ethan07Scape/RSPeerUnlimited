package org.ethan.peer.org.ethan.peer.core;

import org.ethan.peer.callbacks.HttpRequests;
import org.ethan.peer.classloader.ClassArchive;
import org.ethan.peer.handlers.RequestAccountInfo;
import org.ethan.peer.handlers.SDNScriptDownload;
import org.ethan.peer.handlers.SDNScriptList;
import org.ethan.peer.injection.Injector;
import org.ethan.peer.injection.injectors.GetHeaders;
import org.ethan.peer.injection.injectors.PostRequest;
import org.ethan.peer.util.Utilities;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Core {
    /**
     * Currently disabled automatic script dumping due to rate limiting on server-side.
     *
     * You need to own the script/have added to SDN to be able to dump.
     *
     * Make sure you've logged into the RSPeer client it-least once, so it can grab the auth-key for dumping.
     *
     * TODO: add filter for owned scripts vs all scripts to avoid rate limiter
     * TODO: add instance bypass to support multiple clients/instances of scripts.
     *
     */
    private final ClassArchive classArchive;
    private final File rsPeerJar = new File("C:\\Users\\itset\\.rspeer\\1.77.jar");
    private final String outputDir = "C:\\Users\\itset\\Desktop\\Parabot Scripting\\Outputs";
    private final File outputPeer = new File(outputDir + File.separator + "rspeer.jar");
    private boolean ripScripts = true;
    private Map<Integer, String> scriptList = null;

    public Core() {
        System.out.println("RSPeerUnlimited started...");
        this.classArchive = new ClassArchive(this.rsPeerJar, getInjectables());
        dumpInjectedPeer();
        addInjectedPeer();

        runClient();
        System.out.println(new RequestAccountInfo().getAccountInfo());
        if(ripScripts) {
            //ripScriptList();
            new SDNScriptDownload(1128, "bHideTanner", outputDir);
        }


    }

    public static void main(String[] args) {
        new Core();
    }

    private final void runClient() {
        try {
            Class<?> mainClass = Class.forName("org.rspeer.Bootstrap");
            UIManager.put("Classloader", ClassLoader.getSystemClassLoader());
            Constructor constructor = mainClass.getConstructor(new Class[0]);
            Method main = mainClass.getMethod("start", String[].class);
            Object instance = constructor.newInstance(new Object[0]);
            String[] args = {""};
            main.invoke(instance, (Object) args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected List<Injector> getInjectables() {
        List<Injector> injectors = new ArrayList<>();
        injectors.add(new PostRequest());
        injectors.add(new GetHeaders());

        return injectors;
    }
    private void ripScriptList() {
        while(HttpRequests.getAuthKey() == null) {
            sleep();
        }
        scriptList = new SDNScriptList().getScriptList();
       /*     for(Map.Entry<Integer, String> entry : scriptList.entrySet()) {
                new SDNScriptDownload(entry.getKey(), entry.getValue(), outputDir);
                sleepForScriptRip();
            }*/
    }
    private void dumpInjectedPeer() {
        this.classArchive.dump(outputPeer);
    }
    private void addInjectedPeer() {
        Utilities.addToSystemClassLoader(outputPeer);
    }

    private void sleep() {
        try {
            Thread.sleep(250);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
