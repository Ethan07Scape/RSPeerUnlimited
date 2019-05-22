package org.ethan.peer.injection;


import org.objectweb.asm.tree.ClassNode;

public interface Injector {

    boolean condition(ClassNode classNode);

    void inject(ClassNode classNode);
}