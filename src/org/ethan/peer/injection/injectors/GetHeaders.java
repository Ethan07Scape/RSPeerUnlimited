package org.ethan.peer.injection.injectors;


import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.ethan.peer.callbacks.HttpRequests;
import org.ethan.peer.injection.Injector;

import java.util.ListIterator;

public class GetHeaders implements Injector {

    @Override
    public boolean condition(ClassNode classNode) {
        return "com/mashape/unirest/request/HttpRequest".equals(classNode.name);
    }

    @Override
    public void inject(ClassNode classNode) {
        @SuppressWarnings("unchecked")
        ListIterator<MethodNode> mnIt = classNode.methods.listIterator();
        while (mnIt.hasNext()) {
            MethodNode mn = mnIt.next();
            if (mn.name.equals("header")) {
                    inject(mn);

            }
        }
    }

    @SuppressWarnings("deprecation")
    private void inject(MethodNode mn) {
        InsnList nl = new InsnList();
        boolean added = false;
        AbstractInsnNode[] mnNodes = mn.instructions.toArray();
        for (AbstractInsnNode abstractInsnNode : mnNodes) {
            if(!added) {
                nl.add(new VarInsnNode(Opcodes.ALOAD, 1));
                nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HttpRequests.class.getCanonicalName().replace('.', '/'), "printHeader", "(" + "Ljava/lang/String;" + ")V"));
                nl.add(new VarInsnNode(jdk.internal.org.objectweb.asm.Opcodes.ALOAD, 2));
                nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HttpRequests.class.getCanonicalName().replace('.', '/'), "printHeader", "(" + "Ljava/lang/String;" + ")V"));
                System.out.println("Injecting inside GetRequest:Headers.");
                added = true;
            }
            nl.add(abstractInsnNode);
        }

        mn.instructions = nl;
        mn.visitMaxs(0, 0);
        mn.visitEnd();
    }
}
