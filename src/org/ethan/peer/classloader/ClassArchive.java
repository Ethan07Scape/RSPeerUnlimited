package org.ethan.peer.classloader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import org.ethan.peer.injection.Injector;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassArchive {
    public final ArrayList<String> classNames;
    public final HashMap<String, ClassNode> classes;
    public final HashMap<String, byte[]> classesBytes;
    public final HashMap<String, byte[]> resourcesBytes;
    public final Map<String, File> resources;
    private final List<Injector> injectorList;
    private File temp = null;

    public ClassArchive(File file, List<Injector> injectorList) {
        this.classNames = new ArrayList<>();
        this.classes = new HashMap<>();
        this.resources = new HashMap<>();
        this.classesBytes = new HashMap<>();
        this.resourcesBytes = new HashMap<>();
        this.injectorList = injectorList;
        this.addJar(file);

    }

    public ClassArchive(List<Injector> injectorList) {
        this.classNames = new ArrayList<>();
        this.classes = new HashMap<>();
        this.resources = new HashMap<>();
        this.classesBytes = new HashMap<>();
        this.resourcesBytes = new HashMap<>();
        this.injectorList = injectorList;

    }

    protected void loadClass(InputStream in) throws IOException {
        ClassReader cr = new ClassReader(in);
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.EXPAND_FRAMES);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        byte[] b = cw.toByteArray();
        classesBytes.put(cn.name + ".class", b);
        if (!classNames.contains(cn.name.replace('/', '.'))) {
            classNames.add(cn.name.replace('/', '.'));
        }
        if (classes.containsKey(cn.name)) {
            classes.remove(cn.name);
        }
        classes.put(cn.name, inject(cn));

    }

    private ClassNode inject(ClassNode node) {
        if (injectorList != null) {
            for (Injector injector : injectorList) {
                if (injector.condition(node)) {
                    injector.inject(node);
                    return node;
                }
            }
        }
        return node;
    }

    public File getTempDirectory() {
        if (temp != null) {
            return temp;
        }
        int randomNum = new Random().nextInt(999999999);
        temp = new File(System.getProperty("user.home") + "/Parabot/scripts/resources/", randomNum + "/");
        temp.mkdirs();
        temp.deleteOnExit();
        return temp;
    }

    private void loadResource(final String name, final InputStream in)
            throws IOException {
        final File f = File.createTempFile("bot", ".tmp",
                getTempDirectory());
        f.deleteOnExit();
        byte[] fileContent = Files.readAllBytes(f.toPath());
        try (OutputStream out = new FileOutputStream(f)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
        }
        this.resources.put(name, f);
        resourcesBytes.put(name, fileContent);

    }

    public void dump(final File file) {
        System.out.println("Dumping injected version of RSPeer.");
        try {
            dump(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void dump(final FileOutputStream stream) {
        try {
            JarOutputStream out = new JarOutputStream(stream);
            for (ClassNode cn : this.classes.values()) {
                JarEntry je = new JarEntry(cn.name + ".class");
                out.putNextEntry(je);
                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                cn.accept(cw);
                out.write(cw.toByteArray());
            }
            for (Map.Entry<String, File> entry : this.resources.entrySet()) {
                JarEntry je = new JarEntry(entry.getKey());
                out.putNextEntry(je);
                out.write(Files.readAllBytes(entry.getValue().toPath()));
            }
            out.close();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addJar(final File file) {
        try {
            addJar(file.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void addJar(final URL url) {
        try {
            addJar(url.openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addJar(final URLConnection connection) {
        try {
            System.out.println("Loading RSPeer into ClassArchive.");
            final ZipInputStream zin = new ZipInputStream(connection.getInputStream());
            ZipEntry e;
            while ((e = zin.getNextEntry()) != null) {
                if (e.isDirectory())
                    continue;
                if (e.getName().endsWith(".class")) {

                    loadClass(zin);
                } else {
                    loadResource(e.getName(), zin);
                }
            }
            zin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}