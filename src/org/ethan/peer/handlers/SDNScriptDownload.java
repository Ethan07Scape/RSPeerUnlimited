package org.ethan.peer.handlers;


import org.ethan.peer.callbacks.HttpRequests;
import org.ethan.peer.util.GetRequest;
import org.ethan.peer.util.Utilities;

import java.io.*;
import java.util.HashMap;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class SDNScriptDownload {

    private HashMap<String, byte[]> classBytes = new HashMap<>();
    private HashMap<String, byte[]> resourceBytes = new HashMap<>();
    private final int scriptID;
    private final String scriptName;
    private final String outputDir;


    public SDNScriptDownload(int scriptID, String scriptName, String outputDir) {
        this.scriptID = scriptID;
        this.scriptName = scriptName;
        this.outputDir = outputDir;
        dumpSDNScript();
    }

    private byte[] downloadSDNScript() {
        try {
            GetRequest http = new GetRequest("https://services.rspeer.org/api/script/content?id="+scriptID);
            Object getRequest = http.header("Authorization", HttpRequests.getAuthKey());
            Object response = http.getAsBinary(getRequest);
            int status = http.getStatus(response);
            System.out.println("Response status: "+status);
            if (status == 200) {
                final InputStream inputStream = http.getRawBody(response);
                final DataInputStream dataInputStream = new DataInputStream(inputStream);
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataInputStream dataInputStream2 = dataInputStream;
                int read;
                while ((read = dataInputStream2.read()) != -1) {
                    dataInputStream2 = dataInputStream;

                    byteArrayOutputStream.write(read);
                }
                dataInputStream.close();
                final ByteArrayOutputStream byteArrayOutputStream2 = byteArrayOutputStream;
                return byteArrayOutputStream2.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private synchronized Object dumpSDNScript() {
        try {
            byte[] bytes = downloadSDNScript();
            if (bytes != null && bytes.length > 0) {
                System.out.println("Byte Length: " + bytes.length);
            } else {
                System.out.println("Please add script to ScriptSelector: "+scriptName);
                return null;
            }
            final byte[] array = new byte[1024];
            final JarInputStream jarInputStream = new JarInputStream(new ByteArrayInputStream(bytes));
            ZipEntry nextEntry;
            while ((nextEntry = jarInputStream.getNextEntry()) != null) {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JarInputStream jarInputStream2 = jarInputStream;
                int read;
                while ((read = jarInputStream2.read(array, 0, array.length)) != -1) {
                    jarInputStream2 = jarInputStream;
                    byteArrayOutputStream.write(array, 0, read);
                }
                if (nextEntry.getName().endsWith(".class")) {
                    classBytes.put(nextEntry.getName(), byteArrayOutputStream.toByteArray());
                } else {
                    System.out.println(nextEntry.getName());
                    resourceBytes.put(nextEntry.getName(), byteArrayOutputStream.toByteArray());
                }
            }
            System.out.println("Dumping script: "+scriptName);
            Utilities.dumpJar(new File(outputDir + File.separator + scriptName + ".jar"), classBytes, resourceBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
